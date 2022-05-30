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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.componant.LoadingView
import com.brainer.itmmunity.componant.RoundedSurface
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import com.brainer.itmmunity.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentView : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
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
@Preview
@Composable
fun ContentView(
    viewModel: MainViewModel = MainViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val isDarkMode = isSystemInDarkTheme()
    val aNews by viewModel.aNews.observeAsState()
    val contentHtml by viewModel.contentHtml.observeAsState()
    val listState = rememberScrollState()
    val swipeRefreshState by remember { mutableStateOf(true) }

    var textColor = Color(255, 255, 255)
    if (isDarkMode) {
        textColor = Color(255, 255, 255)
    } else if (!isDarkMode) {
        textColor = Color(23, 23, 23)
    }

    LaunchedEffect(aNews) {
        viewModel.changeHtml(null)
        viewModel.getHtml()
    }

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
        RoundedSurface {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = !swipeRefreshState),
                onRefresh = { viewModel.getHtml() }) {
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
}

@Preview
@Composable
fun ContentViewTest() {
    ContentView(MainViewModel())
}
