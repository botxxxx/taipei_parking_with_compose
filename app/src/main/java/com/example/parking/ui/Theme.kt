package com.example.parking.ui

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout

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
fun BasicsTheme(
    isLoading: Boolean = false,
    content: @Composable (Modifier) -> Unit,
) {
    val typography = LightTypography
    MaterialTheme(
        colors = lightColorPalette,
        typography = typography,
        shapes = shapes,
        content = {
            ConstraintLayout {
                val (body, progress) = createRefs()
                content(
                    Modifier.constrainAs(body) {
                        top.linkTo(parent.top)
                    }
                )
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.constrainAs(progress) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            }
        }
    )
}