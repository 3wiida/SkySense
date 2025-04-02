package com.ewida.skysense.saved.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ewida.skysense.R

@Composable
fun SavedPlacesEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.empty_saved_places_imgsvg),
            contentDescription = null
        )

        Text(
            text = stringResource(R.string.no_saved_locations_yet),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp),
            text = stringResource(R.string.empty_saved_places_description),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}