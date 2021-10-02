package com.brainer.itmmunity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import kotlinx.coroutines.*

lateinit var intent: Intent

class ContentView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var contentHtml: String? = null

        val aNews = intent.getParcelableExtra<Croll.Content>("content")

        GlobalScope.launch {
            contentHtml = aNews?.let { aNews.returnContent(it).toString() } + FIT_IMAGE_SCRIPT
            Log.d("contentHTML", contentHtml!!)

            setContent {
                ITmmunity_AndroidTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        contentView(contentHtml)
                    }
                }
            }
        }
        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    contentView(null)
                }
            }
        }
    }
}

@Composable
fun contentView(contentHtml: String?) {
    val isDarkMode = isSystemInDarkTheme()

    if (contentHtml == null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 10.dp)) {
            LoadingView()
        }
    } else {
        AndroidView(modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadData(contentHtml, "text/html", "utf-8")
                }
            }, update = {
                it.settings.javaScriptEnabled = true
                it.loadData(contentHtml, "text/html", "utf-8")

//                    it.getSettings().layoutAlgorithm = LayoutAlgorithm.SINGLE_COLUMN

                it.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                it.isHorizontalScrollBarEnabled = false;

//                    it.isHorizontalScrollBarEnabled = false;

//                    it.settings.useWideViewPort = true
                it.settings.loadWithOverviewMode = true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (isDarkMode) {
                        it.settings.forceDark = WebSettings.FORCE_DARK_ON
                    } else {
                        it.settings.forceDark = WebSettings.FORCE_DARK_OFF
                    }
                }
            })
    }
}
