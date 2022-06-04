package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.brainer.itmmunity.viewmodel.ContentViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.jeziellago.compose.markdowntext.MarkdownText

//class ContentView : ComponentActivity() {
//    @SuppressLint("CoroutineCreationDuringComposition")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val viewModel = MainViewModel()
//        val aNews = intent.getParcelableExtra<Croll.Content>("content")
//
//        CoroutineScope(Dispatchers.Main).launch {
//            setContent {
//                ITmmunity_AndroidTheme {
//                    // A surface container using the 'background' color from the theme
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//                        if (aNews != null) {
//                            ContentView(viewModel)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun ContentView(
    aNews: Croll.Content,
    contentViewModel: ContentViewModel = ContentViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val isDarkMode = isSystemInDarkTheme()
//    val isTabletUi by viewModel.isTabletUi.collectAsState()
//    val aNewsState by mutableStateOf(viewModel.aNews.collectAsState())
//    val aNews by rememberSaveable { aNewsState }
    val contentHtmlState by mutableStateOf(contentViewModel.contentHtml.collectAsState())
    val contentHtml by rememberSaveable { contentHtmlState }
    val listState = rememberScrollState()
    val swipeRefreshState by remember { mutableStateOf(true) }

    var textColor = Color(255, 255, 255)
    if (isDarkMode) {
        textColor = Color(255, 255, 255)
    } else if (!isDarkMode) {
        textColor = Color(23, 23, 23)
    }

    contentViewModel.setContent(aNews)

//    LaunchedEffect(aNews) {
//        contentViewModel.getHtml()
//    }

//    LaunchedEffect(isTabletUi) {
//
//    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        RoundedSurface() {
            val boxWithConstraintsScope = this
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = !swipeRefreshState),
                onRefresh = { contentViewModel.getHtml() }) {
                AnimatedContent(targetState = contentHtml) { targetHtml ->
                    if (targetHtml.isEmpty()) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LoadingView()
                        }
                    } else {
                        kotlin.runCatching {
                            SelectionContainer(Modifier.fillMaxSize()) {
                                Column(Modifier.verticalScroll(listState)) {
                                    Row(
                                        Modifier
                                            .height(64.dp)
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        aNews.let {
                                            Text(
                                                modifier = Modifier.weight(9f),
                                                text = it.title,
                                                fontSize = 20.sp,
                                                textAlign = TextAlign.Left,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        val context = LocalContext.current
                                        Icon(
                                            Icons.Filled.Share,
                                            contentDescription = "공유",
                                            Modifier
                                                .weight(1f)
                                                .clickable {
                                                    val sendIntent: Intent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(Intent.EXTRA_TITLE, aNews!!.title)
                                                        putExtra(
                                                            Intent.EXTRA_SUBJECT,
                                                            "Powered by ITmmunity"
                                                        )
                                                        putExtra(Intent.EXTRA_TEXT, aNews!!.url)
                                                        type = "text/plain"
                                                    }

                                                    val shareIntent =
                                                        Intent.createChooser(sendIntent, null)
                                                    context.startActivity(shareIntent)
                                                })
                                    }

                                    MarkdownText(
                                        modifier = Modifier.padding(8.dp),
                                        markdown = targetHtml,
                                        color = textColor
                                    )
                                    Log.v("contentHtml", targetHtml)
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

@ExperimentalAnimationApi
@Preview
@Composable
fun ContentViewPreview() {
    ContentView(Croll.Content(
        title = "안녕하세요",
        hit = 0,
        url = "http://underkg.co.kr/news/2897268"
    ))
}
