package com.brainer.itmmunity.Utility

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getSurfaceColor(): Color {
    val backGroundColor = if (isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color(245, 244, 244)
    }
    return backGroundColor
}

@Composable
fun getTextColor(): Color {
    var textColor = Color(255, 255, 255)

    if (isSystemInDarkTheme()) {
        textColor = Color(255, 255, 255)
    } else {
        textColor = Color(23, 23, 23)
    }

    return textColor
}
