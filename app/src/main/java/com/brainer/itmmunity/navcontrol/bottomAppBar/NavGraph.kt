package com.brainer.itmmunity.navcontrol.bottomAppBar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavController
) {
    AnimatedNavHost(
        navController = navController as NavHostController,
        startDestination = NavScreen.News.route
    ) {
        composable(
            route = NavScreen.News.route,
            enterTransition = ComposableTransition.News.enterTransition,
            exitTransition = ComposableTransition.News.exitTransition,
            popEnterTransition = ComposableTransition.News.popEnterTransition,
            popExitTransition = ComposableTransition.News.popExitTransition,
        ) {

        }

        composable(
            route = NavScreen.Freeboard.route,
            enterTransition = ComposableTransition.Freeboard.enterTransition,
            exitTransition = ComposableTransition.Freeboard.exitTransition,
            popEnterTransition = ComposableTransition.Freeboard.popEnterTransition,
            popExitTransition = ComposableTransition.Freeboard.popExitTransition,
        ) {

        }
    }
}
