package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.util.formatToDefaultLocale

@Composable
fun WindCard(
    windSpeed: Double,
    windDeg: Int
) {
    Box(
        modifier = Modifier
            .height(175.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.wind),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.m_s, windSpeed.formatToDefaultLocale()),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier.align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(72.dp).rotate(windDeg.toFloat()),
                painter = painterResource(R.drawable.ic_top_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

    }

}