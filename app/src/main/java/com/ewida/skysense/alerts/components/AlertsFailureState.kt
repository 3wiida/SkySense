package com.ewida.skysense.alerts.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R

@Composable
fun AlertsFailureState(
    cause: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 32.dp),
            painter = painterResource(R.drawable.failure_img),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.failure_main_text),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp)
                .padding(horizontal = 24.dp),
            text = cause,
            textAlign = TextAlign.Center,
            color = Color(0xFFA1A1A1),
            style = MaterialTheme.typography.labelMedium,
            fontSize = 16.sp
        )
    }
}