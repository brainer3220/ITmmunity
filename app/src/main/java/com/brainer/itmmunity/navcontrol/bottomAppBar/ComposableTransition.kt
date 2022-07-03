package com.brainer.itmmunity.navcontrol.bottomAppBar

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavBackStackEntry

@ExperimentalAnimationApi
sealed class ComposableTransition(
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition)?,
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    val popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition
) {
    object News : ComposableTransition(
        enterTransition = null,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = null
    )

    object Freeboard : ComposableTransition(
        enterTransition = null,
        exitTransition = null,
        popEnterTransition = null,
        popExitTransition = null
    )
}
