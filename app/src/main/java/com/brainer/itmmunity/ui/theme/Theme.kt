package com.brainer.itmmunity.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = Color.Blue,
//    primaryVariant = Color.Blue,
    secondary = Color.Blue
)

private val LightColorPalette = lightColorScheme(
    primary = Color.Blue,
//    primaryVariant = Color.Blue,
    secondary = Color.Blue
)

@Composable
fun ITmmunity_AndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
//        systemUiController.setSystemBarsColor(
//            //Set app bar color for dark theme
//            color = Color.Red
//        )
        DarkColorPalette
    } else {
//        systemUiController.setSystemBarsColor(
//            //Set app bar color for light theme
//            color = Color.Blue
//        )
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
