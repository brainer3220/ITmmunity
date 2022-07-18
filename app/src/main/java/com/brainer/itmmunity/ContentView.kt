package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.Utility.getSurfaceColor
import com.brainer.itmmunity.Utility.getTextColor
import com.brainer.itmmunity.componant.AdMobCompose
import com.brainer.itmmunity.componant.LoadingView
import com.brainer.itmmunity.componant.RoundedSurface
import com.brainer.itmmunity.viewmodel.ContentViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.jeziellago.compose.markdowntext.MarkdownText


@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun ContentView(
    aNews: Croll.Content,
    contentViewModel: ContentViewModel = ContentViewModel()
) {
    val isDarkMode = isSystemInDarkTheme()
//    val isTabletUi by viewModel.isTabletUi.collectAsState()
//    val aNewsState by mutableStateOf(viewModel.aNews.collectAsState())
//    val aNews by rememberSaveable { aNewsState }
    val contentHtmlState by mutableStateOf(contentViewModel.contentHtml.collectAsState())
    val contentHtml by rememberSaveable { contentHtmlState }
    val listState = rememberScrollState()
    val swipeRefreshState by remember { mutableStateOf(true) }

    val backGroundColor = getSurfaceColor()
    val textColor = getTextColor()

    contentViewModel.setContent(aNews)

    RoundedSurface {
        BoxWithConstraints(Modifier) {
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
                            SelectionContainer(Modifier) {
                                Column(
                                    Modifier
                                        .verticalScroll(listState)
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = aNews.title,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Left
                                    )

                                    MarkdownText(
                                        modifier = Modifier.padding(top = 16.dp),
                                        markdown = targetHtml,
                                        color = textColor
                                    )

                                    AdMobCompose(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 50.dp),
                                        adId = "ca-app-pub-1000428004132415/8906506129"
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
fun ContentViewTest() {
    ContentView(
        dummies[0]
    )
}
