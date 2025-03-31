package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import com.ewida.skysense.util.formatToDefaultLocale

@Composable
fun FeelsLikeCard(
    feelsLikeTemp: Int = 7
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
                text = stringResource(R.string.feels_like),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = feelsLikeTemp.formatToDefaultLocale(),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier.align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(72.dp)
            ) {
                drawArc(
                    startAngle = 140f,
                    sweepAngle = 120f,
                    useCenter = false,
                    color = Color(0xFF1c92d2),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )

                drawArc(
                    startAngle = 268f,
                    sweepAngle = 48f,
                    useCenter = false,
                    color = Color(0xFF019628),
                    style = Stroke(width = 4.dp.toPx())
                )

                drawArc(
                    startAngle = 323f,
                    sweepAngle = 80f,
                    useCenter = false,
                    color = Color(0xFFFC963C),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Icon(
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

    }

}