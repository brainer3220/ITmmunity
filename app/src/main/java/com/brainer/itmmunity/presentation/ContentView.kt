package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.data.Croll.Croll
import com.brainer.itmmunity.presentation.componant.AdMobCompose
import com.brainer.itmmunity.presentation.componant.LoadingView
import com.brainer.itmmunity.presentation.componant.RoundedSurface
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel
import com.brainer.itmmunity.utility.getSurfaceColor
import com.brainer.itmmunity.utility.getTextColor
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun ContentView(
    aNews: Croll.Content,
    contentViewModel: ContentViewModel = ContentViewModel(),
) {
    val scope = rememberCoroutineScope()
    val contentHtmlState by mutableStateOf(contentViewModel.contentHtml.collectAsState())
    val contentHtml by rememberSaveable { contentHtmlState }
    val listState = rememberScrollState()
    val swipeRefreshState by remember { mutableStateOf(true) }

    val backGroundColor = getSurfaceColor()
    val textColor = getTextColor()

    contentViewModel.setContent(aNews)

    RoundedSurface {
        if (contentHtml.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingView()
            }
        }
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = !swipeRefreshState),
            onRefresh = { contentViewModel.getHtml() },
        ) {
            Column(
                modifier = Modifier
                    .background(backGroundColor)
                    .fillMaxSize()
                    .verticalScroll(listState),
            ) {
                RoundedSurface {
                    BoxWithConstraints {
                        kotlin.runCatching {
                            SelectionContainer(Modifier) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .animateContentSize()
                                        .padding(20.dp),
                                ) {
                                    Text(
                                        text = aNews.title,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Left,
                                    )

                                    MarkdownText(
                                        modifier = Modifier.padding(top = 16.dp),
                                        markdown = contentHtml,
                                        color = textColor,
                                    )

                                        Log.v("contentHtml", contentHtml)
                                    }
                                }
                            }.onFailure {
                                Log.w("contentHtml", it.toString())
                            }
                        }
                    }

                Spacer(
                    modifier = Modifier
                        .padding(16.dp)
                        .alpha(0.0f),
                )

                RoundedSurface {
                    AdMobCompose(
                        modifier = Modifier.padding(12.dp),
                        adId = "ca-app-pub-3940256099942544/6300978111",
                    )
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
        dummies[0],
    )
}
