package com.example.parking.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightColorPalette = lightColors(
    background = Color.White,
    onBackground = black_500,
    surface = black_500,
    secondary = forest,
    primary = forest_green,
    primaryVariant = apple,
    onPrimary = Color.White,
    onSecondary = Color.White,
)

@Composable
fun BasicsCodeLabTheme(
    content: @Composable () -> Unit
) {
    val typography = LightTypography
    MaterialTheme(
        colors = lightColorPalette,
        typography = typography,
        shapes = shapes,
        content = content
    )
}