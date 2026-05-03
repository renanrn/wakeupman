package com.wakeupman.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AlertYellow,
    secondary = WarningOrange,
    tertiary = EmergencyRed,
    background = CarbonBlack,
    surface = IndustrialGray,
    onPrimary = CarbonBlack,
    onSecondary = CarbonBlack,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = EmergencyRed,
    surfaceVariant = DimGray
)

@Composable
fun WakeUpManTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CarbonBlack.toArgb()
            window.navigationBarColor = CarbonBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
