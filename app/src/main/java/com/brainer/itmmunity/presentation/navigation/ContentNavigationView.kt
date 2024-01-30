package com.brainer.itmmunity.presentation.navigation

import android.app.Application
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.brainer.itmmunity.ContentView
import com.brainer.itmmunity.content
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel

@OptIn(ExperimentalAnimationApi::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ContentNavigationView(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "content",
    ) {
        composable("content") {
            ContentView(content, ContentViewModel(Application()), navController)
        }
        composable(
            "webview/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType }),
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")
            Log.i("ContentNavigationView", "url: $url")
            AndroidView(factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    loadUrl(url ?: "")
                }
            })
        }
    }
}
