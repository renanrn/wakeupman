package com.wakeupman.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD600), // Alert Yellow
    secondary = Color(0xFF1A1A1A),
    tertiary = Color(0xFFBCB141),
    background = Color(0xFF0A0A0A), // Carbon Black
    surface = Color(0xFF0A0A0A),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFFF2400) // Emergency Red
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFD600),
    secondary = Color(0xFF1A1A1A),
    tertiary = Color(0xFFBCB141),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFFF2400)
)

@Composable
fun WakeUpManTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
