package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import kotlinx.coroutines.*

class ContentView : ComponentActivity() {
    private lateinit var contentHtml: String

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val aNews = intent.getParcelableExtra<Croll.Content>("content")

        GlobalScope.launch {
            contentHtml = aNews?.let { aNews.returnContent(it).toString() } + FIT_IMAGE_SCRIPT
            Log.d("contentHTML", contentHtml)

            setContent {
                ITmmunity_AndroidTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        aNews?.let { contentView(contentHtml, it) }
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
                    contentView(null, null)
                }
            }
        }
    }

    override fun onDestroy() {
        contentHtml = ""
        super.onDestroy()
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun contentView(contentHtml: String?, aNews: Croll.Content?) {
    val isDarkMode = isSystemInDarkTheme()

    if (contentHtml == null) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 10.dp)) {
            LoadingView()
        }
    } else {
        Column() {
            Box(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            ) {
                Column() {
                    if (aNews != null) {
                        SelectionContainer {
                            Text(
                                modifier = Modifier,
                                text = aNews.title,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Left,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
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

                    it.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                    it.isHorizontalScrollBarEnabled = false;

//                it.settings.useWideViewPort = true
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
}
