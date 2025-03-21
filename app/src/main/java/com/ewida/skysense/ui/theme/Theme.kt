package com.ewida.skysense.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val LightColorPalette = lightColorScheme(
    primary = Color(0xFF8BAADF),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFffffff),
    onSurface = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFd8d9da),
    outline = Color(0xFFa2bef2),
    onBackground = Color(0xFF474747),
    outlineVariant = Color(0x14A2BEF2),
    onPrimary = Color(0xFFf8f9f9),
    error = Color(0xFFF57575)
)

@Composable
fun SkySenseTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}