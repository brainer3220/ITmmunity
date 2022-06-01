package com.brainer.itmmunity.navcontrol

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.brainer.itmmunity.ANIMATION_DURATION
import com.brainer.itmmunity.ANIMATION_INIT_OFFSET_Y
import com.brainer.itmmunity.ANIMATION_TARGET_OFFSET_Y

@ExperimentalAnimationApi
sealed class ComposableTransition(
    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition),
    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)?,
    val popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = enterTransition,
    val popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = exitTransition
) {
    object Main : ComposableTransition(
        enterTransition = {
            when (initialState.destination.route) {
                "Red" ->
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                else -> {
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                }
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "Blue" ->
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                "Blue" ->
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                "Blue" ->
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(ANIMATION_DURATION)
                    )
                else -> null
            }
        }
    )

    object ContentView : ComposableTransition(
        enterTransition = {
            slideInVertically(initialOffsetY = { ANIMATION_INIT_OFFSET_Y }) + fadeIn()
        },
        exitTransition = {
            slideOutVertically(targetOffsetY = { ANIMATION_TARGET_OFFSET_Y }) + fadeOut()
        },
        popEnterTransition = {
            slideInVertically(initialOffsetY = { ANIMATION_INIT_OFFSET_Y }) + fadeIn()
        },
        popExitTransition = {
            slideOutVertically(targetOffsetY = { ANIMATION_TARGET_OFFSET_Y }) + fadeOut()
        }
    )
}
