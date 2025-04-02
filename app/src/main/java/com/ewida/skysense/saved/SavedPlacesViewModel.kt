package com.ewida.skysense.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.util.enums.WeatherUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedPlacesViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _savedPlaces = MutableStateFlow<List<WeatherDetails>>(emptyList())
    val savedPlaces = _savedPlaces.asStateFlow()

    init {
        getSavedPlaces()
    }

    fun getSavedPlaces() {
        viewModelScope.launch {
            repo.getSavedPlacesDetails().collect { response ->
                _savedPlaces.emit(response)
            }
        }
    }

    fun getUnit(): WeatherUnit {
        return repo.getAppSettings().unit
    }

    fun deletePlace(place: WeatherDetails){
        viewModelScope.launch {
            repo.deleteSavedPlace(place)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class SavedPlacesViewModelFactory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SavedPlacesViewModel(repo = repository) as T
        }
    }
}