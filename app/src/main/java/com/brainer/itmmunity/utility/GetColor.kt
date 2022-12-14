package com.brainer.itmmunity.utility

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
fun getBackgroundColor(): Color {
    val backGroundColor = if (isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color(245, 244, 244)
    }

    return backGroundColor
}

@Composable
fun getTextColor(): Color {
    val textColor = if (isSystemInDarkTheme()) {
        Color(255, 255, 255)
    } else {
        Color(23, 23, 23)
    }

    return textColor
}
