package com.brainer.itmmunity.navcontrol

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.brainer.itmmunity.*
import com.brainer.itmmunity.viewmodel.BackGroundViewModel
import com.brainer.itmmunity.viewmodel.MainViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


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

@ExperimentalAnimationApi
@Composable
fun NavGraph(
    navController: NavController,
    viewModel: MainViewModel,
    networkViewModel: BackGroundViewModel
) {
    AnimatedNavHost(
        navController = navController as NavHostController,
        startDestination = NavScreen.MainList.route
    ) {
        composable(
            route = NavScreen.MainList.route,
            enterTransition = ComposableTransition.Main.enterTransition,
            exitTransition = ComposableTransition.Main.exitTransition,
            popEnterTransition = ComposableTransition.Main.popEnterTransition,
            popExitTransition = ComposableTransition.Main.popExitTransition
        ) {
            MainView(
                viewModel = viewModel,
                networkViewModel = networkViewModel,
                navController = navController
            )
        }

        composable(
            route = NavScreen.ContentView.route,
            arguments = listOf(navArgument(DEFAULT_NEWS_URL) {
                type = NavType.StringType
            }),
            enterTransition = ComposableTransition.ContentView.enterTransition,
            exitTransition = ComposableTransition.ContentView.exitTransition,
            popEnterTransition = ComposableTransition.ContentView.popEnterTransition,
            popExitTransition = ComposableTransition.ContentView.popExitTransition
        )
        { ContentView(viewModel = viewModel) }
    }
}
