package com.brainer.itmmunity.navcontrol

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.brainer.itmmunity.*
import com.brainer.itmmunity.viewmodel.BackGroundViewModel
import com.brainer.itmmunity.viewmodel.MainViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

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
        composable(route = NavScreen.MainList.route,
            enterTransition = {
                when (initialState.destination.route) {
                    "Red" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(ANIMATION_DURATION)
                        )
                    else -> null
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
            }) {
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
            }) { ContentView(viewModel = viewModel) }
    }
}