package com.brainer.itmmunity.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.brainer.ITmmunity.ui.theme.Primary
import com.brainer.ITmmunity.ui.theme.themeOFDarkPrimary

private val DarkColorPalette = darkColors(
    primary = themeOFDarkPrimary
//    primaryVariant = purple700,
//    secondary = DarkWhite
)

private val LightColorPalette = lightColors(
    primary = Primary
//    primaryVariant = purple700,
//    secondary = AllWhite

/* Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/
)

@Composable
fun DattaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}