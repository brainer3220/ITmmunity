package com.brainer.itmmunity.presentation.navigation

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.brainer.itmmunity.MainViewWithAppBar
import com.brainer.itmmunity.presentation.viewmodel.MainViewModel

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationView(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainViewWithAppBar(
                viewModel = viewModel(MainViewModel::class.java),
                networkViewModel = viewModel(),
            )
        }
        composable("webview", arguments = listOf()) {
            AndroidView(factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadUrl(it.arguments?.getString("url") ?: "")
                }
            })
        }
    }
}
