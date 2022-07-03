package com.brainer.itmmunity.navcontrol.bottomAppBar

sealed class NavScreen(val route: String) {
    object News : NavScreen("/news")
    object Freeboard : NavScreen("/freeboard")
}
