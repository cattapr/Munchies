package com.example.munchies.feature.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Selected,
    onPrimary = LightText,
    background = Background,
    onBackground = DarkText,
    surface = LightText,
    onSurface = DarkText,
    error = Negative,
    onError = LightText,
    secondary = Subtitle,
    onSecondary = LightText,
    tertiary = Positive,
    onTertiary = LightText
)

@Composable
fun MunchiesApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}