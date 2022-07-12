package com.brainer.itmmunity.navcontrol

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.brainer.itmmunity.ContentView
import com.brainer.itmmunity.MainView
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
        composable(
            route = NavScreen.MainList.route,
            enterTransition = ComposableTransition.Main.enterTransition,
            exitTransition = ComposableTransition.Main.exitTransition,
            popEnterTransition = ComposableTransition.Main.popEnterTransition,
            popExitTransition = ComposableTransition.Main.popExitTransition
        ) {
            MainView(
                viewModel = viewModel,
                networkViewModel = networkViewModel
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
        { viewModel.aNews.collectAsState().value?.let { it1 -> ContentView(aNews = it1) } }
    }
}
