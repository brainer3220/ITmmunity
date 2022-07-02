package com.brainer.itmmunity.navcontrol

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.brainer.itmmunity.ANIMATION_DURATION

@ExperimentalAnimationApi
sealed class ComposableTransition(
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition),
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    val popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition
) {
    object Main : ComposableTransition(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(ANIMATION_DURATION)
            )
        }
    )

    object ContentView : ComposableTransition(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(ANIMATION_DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = tween(ANIMATION_DURATION)
            )
        }
    )
}
