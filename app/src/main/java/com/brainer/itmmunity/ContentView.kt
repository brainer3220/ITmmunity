package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.brainer.itmmunity.Componant.LoadingView
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.ViewModel.ContentViewModel
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContentView : ComponentActivity() {
    private lateinit var contentHtml: String

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ContentViewModel()

        val aNews = intent.getParcelableExtra<Croll.Content>("content")

        GlobalScope.launch {
            if (aNews != null) {
                viewModel.getHtml(aNews)
            }

            setContent {
                ITmmunity_AndroidTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        ContentView(viewModel)
                    }
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
fun ContentView(viewModel: ContentViewModel = ContentViewModel()) {
    val isDarkMode = isSystemInDarkTheme()

    val contentHtml by viewModel.contentHtml.observeAsState()
    val aNews by viewModel.aNews.observeAsState()

    if (contentHtml == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp)
        ) {
            LoadingView()
        }
    } else {
        Column {
            Row {
                TopAppBar(title = {
                    Text(
                        text = aNews!!.title,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                    actions = {
                        val context = LocalContext.current
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "공유",
                            Modifier.clickable {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TITLE, aNews?.title.toString())
                                    putExtra(Intent.EXTRA_SUBJECT, "Powered by ITmmunity")
                                    putExtra(Intent.EXTRA_TEXT, aNews?.url)
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            })
                    })
                Spacer(modifier = Modifier.padding(4.dp))
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
                        loadData(contentHtml!!, "text/html", "utf-8")
                    }
                }, update = {
                    it.settings.javaScriptEnabled = true
                    it.loadData(contentHtml!!, "text/html", "utf-8")

                    it.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                    it.isHorizontalScrollBarEnabled = false

//                it.settings.useWideViewPort = true
                    it.settings.loadWithOverviewMode = true

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (isDarkMode) {
                            it.settings.forceDark = WebSettings.FORCE_DARK_ON
                        } else {
                            it.settings.forceDark = WebSettings.FORCE_DARK_OFF
                        }
                    }
                }
            )
        }
    }
}
