package com.brainer.itmmunity.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.brainer.itmmunity.MainViewWithAppBar
import com.brainer.itmmunity.presentation.viewmodel.MainViewModel
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun NavigationView(bottomSheetNavigator: BottomSheetNavigator, navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainViewWithAppBar(
                viewModel = viewModel(MainViewModel::class.java),
                networkViewModel = viewModel(),
            )
        }
    }
}
