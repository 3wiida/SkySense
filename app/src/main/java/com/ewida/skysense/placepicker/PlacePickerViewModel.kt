package com.ewida.skysense.placepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ewida.skysense.data.repository.WeatherRepository
import com.ewida.skysense.util.ActionResult
import com.ewida.skysense.util.enums.AppLanguage
import com.ewida.skysense.util.enums.SourceScreen
import com.ewida.skysense.util.roundTo
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class PlacePickerViewModel(
    private val repo: WeatherRepository,
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _searchQuery = MutableSharedFlow<String>(replay = 1)

    private val _searchResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _predictedPlaceLatLng = MutableStateFlow<LatLng?>(null)
    val predictedPlaceLatLng = _predictedPlaceLatLng.asStateFlow()

    private val _isPlaceSaved = MutableStateFlow(ActionResult.IDLE)
    val isPlaceSaved = _isPlaceSaved.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collectLatest { query -> searchForPlaces(placesClient, query) }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        viewModelScope.launch {
            _searchQuery.emit(newQuery)
        }
    }

    fun onPredictionSelected(placeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchPlaceDetails(
                placesClient = placesClient,
                placeId = placeId
            ).addOnSuccessListener { response ->
                _predictedPlaceLatLng.value = response.place.latLng
            }.addOnFailureListener {
                _predictedPlaceLatLng.value = null
            }
        }
    }

    fun onSaveClicked(place: LatLng, sourceScreen: SourceScreen) {
        when(sourceScreen){
            SourceScreen.SAVED -> savePlace(place)
            SourceScreen.SETTINGS -> updateMainPlace(place)
        }

    }

    private fun savePlace(place: LatLng){
        _isPlaceSaved.value = ActionResult.LOADING
        viewModelScope.launch {
            repo.getWeatherDetails(
                latitude = place.latitude.roundTo(2),
                longitude = place.longitude.roundTo(2),
                lang = getLanguage()
            ).catch { throwable ->
                _isPlaceSaved.value = ActionResult.FAILED
            }.collect { _ ->
                _isPlaceSaved.value = ActionResult.COMPLETED
            }
        }
    }

    private fun updateMainPlace(place: LatLng){
        repo.saveMapLocation(place)
        _isPlaceSaved.value = ActionResult.COMPLETED
    }

    private fun getLanguage(): String {
        return when (repo.getAppSettings().language) {
            AppLanguage.ENGLISH -> "en"
            AppLanguage.ARABIC -> "ar"
        }
    }

    fun searchForPlaces(
        placesClient: PlacesClient,
        query: String
    ) {
        repo.fetchPlacePredictions(
            placesClient = placesClient,
            query = query
        ).addOnSuccessListener { response ->
            _searchResults.value = response.autocompletePredictions
        }.addOnFailureListener {
            _searchResults.value = emptyList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class PlacePickerViewModelFactory(
        private val repository: WeatherRepository,
        private val placesClient: PlacesClient
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlacePickerViewModel(
                repo = repository,
                placesClient = placesClient
            ) as T
        }
    }
}