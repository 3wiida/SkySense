package com.ewida.skysense.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ewida.skysense.R

@Composable
fun ScreenHeader(
    title: String,
    onBackClicked: () -> Unit
) {
    val layoutDirection = LocalLayoutDirection.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = onBackClicked
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(
                    when(layoutDirection){
                        LayoutDirection.Ltr ->  R.drawable.ic_back_left
                        LayoutDirection.Rtl ->  R.drawable.ic_back_right
                    }
                ),
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}