package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.data.Croll.Croll
import com.brainer.itmmunity.presentation.componant.AdMobCompose
import com.brainer.itmmunity.presentation.componant.CustomPullRefreshIndicator
import com.brainer.itmmunity.presentation.componant.LoadingView
import com.brainer.itmmunity.presentation.componant.RoundedSurface
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel
import com.brainer.itmmunity.presentation.viewmodel.LoadState
import com.brainer.itmmunity.utility.getSurfaceColor
import com.brainer.itmmunity.utility.getTextColor
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun ContentView(
    aNews: Croll.Content,
    contentViewModel: ContentViewModel,
) {
    val scope = rememberCoroutineScope()
    val contentHtmlState by contentViewModel.contentHtml.collectAsState()
    val scrollState = rememberScrollState()
    val loadState by contentViewModel.loadState.collectAsState()
    val refreshing = remember { mutableStateOf(false) }
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = refreshing.value,
            onRefresh = { contentViewModel.getHtml() },
        )

    val backGroundColor = getSurfaceColor()
    val textColor = getTextColor()

    contentViewModel.setContent(aNews)

    LaunchedEffect(loadState) {
        refreshing.value = loadState == LoadState.LOADING
    }

    RoundedSurface {
        if (contentHtmlState.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingView()
            }
        }
        Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
            Column(
                modifier = Modifier
                    .background(backGroundColor)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
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
                                        markdown = contentHtmlState,
                                        color = textColor,
                                    )

                                    Log.v("contentHtml", contentHtmlState)
                                }
                            }
                        }.onFailure {
                            Log.e("contentHtml", it.toString())
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
            val showButton by remember {
                derivedStateOf {
                    scrollState.value > 0
                }
            }
            AnimatedVisibility(
                visible = showButton,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(onClick = {
                    scope.launch {
                        scrollState.animateScrollTo(0)
                    }
                }) {
                    Icon(
                        imageVector = (Icons.Default.KeyboardArrowUp),
                        contentDescription = "위로",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(42.dp),
                    )
                }
            }
            CustomPullRefreshIndicator(refreshing.value, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun ContentViewTest() {
    ContentView(
        dummies[0],
        ContentViewModel(Application()),
    )
}
