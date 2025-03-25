package com.ewida.skysense.weatherdetails.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource

@Composable
fun PressureCard(
    pressure: Int = 1018,

) {
    val minPressure = 850f
    val maxPressure = 1100f
    val normalizedPressure = ((pressure - minPressure) / (maxPressure - minPressure)).coerceIn(0f, 1f)
    val sweepAngle = normalizedPressure * 240f

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
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(
                text = stringResource(R.string.pressure),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "$pressure",
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
                    startAngle = 150f,
                    sweepAngle = 240f,
                    useCenter = false,
                    color = Color(0xFFF1F1F1),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )

                drawArc(
                    startAngle = 150f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    color = Color(0xFF1c92d2),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pressure),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Text(
                    text = "mbar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFA6A6A6)
                )
            }
        }
    }

}