package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.Componant.LoadingView
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.ViewModel.MainViewModel
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContentView : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MainViewModel()
        val aNews = intent.getParcelableExtra<Croll.Content>("content")

        CoroutineScope(Dispatchers.Main).launch {
            setContent {
                ITmmunity_AndroidTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        if (aNews != null) {
                            ContentView(viewModel)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ContentView(viewModel: MainViewModel) {
    val isDarkMode = isSystemInDarkTheme()
    val aNews by viewModel.aNews.observeAsState()
    val contentHtml by viewModel.contentHtml.observeAsState()
    val listState = rememberScrollState()

    var textColor = Color(255, 255, 255)
    if (isDarkMode) {
        textColor = Color(255, 255, 255)
    } else if (!isDarkMode) {
        textColor = Color(23, 23, 23)
    }

    LaunchedEffect(aNews) {
        viewModel.getHtml()
    }

    Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(25.dp)) {
        Column {
            Row {
                SmallTopAppBar(title = {
                    aNews?.let {
                        Text(
                            text = it.title,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Left,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                    actions = {
                        val context = LocalContext.current
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "공유",
                            Modifier.clickable {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TITLE, aNews!!.title)
                                    putExtra(Intent.EXTRA_SUBJECT, "Powered by ITmmunity")
                                    putExtra(Intent.EXTRA_TEXT, aNews!!.url)
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            })
                    })
                Spacer(modifier = Modifier.padding(4.dp))
            }
            AnimatedContent(targetState = contentHtml) { targetHtml ->
                if (targetHtml == null || targetHtml.isEmpty()) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        LoadingView()
                    }
                } else {
                    kotlin.runCatching {
                        SelectionContainer(Modifier.fillMaxSize()) {
                            Column(Modifier.verticalScroll(listState)) {
                                MarkdownText(
                                    modifier = Modifier.padding(8.dp),
                                    markdown = targetHtml,
                                    color = textColor
                                )
                                Log.v("contentHtml", targetHtml.toString())
                            }
                        }
                    }.onFailure {
                        Log.w("contentHtml", it.toString())
                    }
                }
            }
        }
    }
}
