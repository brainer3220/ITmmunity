@file:OptIn(ExperimentalMaterial3Api::class)

package com.brainer.itmmunity.presentation.componant

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.brainer.itmmunity.ContentActivity
import com.brainer.itmmunity.ContentView
import com.brainer.itmmunity.R
import com.brainer.itmmunity.domain.model.ContentModel
import com.brainer.itmmunity.dummies
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel
import com.brainer.itmmunity.presentation.viewmodel.MainViewModel
import com.brainer.itmmunity.utility.getBackgroundColor
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.circular.CircularRevealPlugin
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val DEFAULT_LOTTIE_VIEW_URL = "https://assets2.lottiefiles.com/packages/lf20_wfsunjgd.json"
const val LOTTIE_IMG_VIEW_URL = "https://assets2.lottiefiles.com/packages/lf20_6odgh2c6.json"
const val ROUNDED_VALUE = 26

@Preview
@Composable
fun LoadingView(
    lottieURL: String? = DEFAULT_LOTTIE_VIEW_URL,
    size: Int = 72,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieURL!!))

    if (composition == null) {
        CircularProgressIndicator(
            Modifier.size(size.dp).fillMaxWidth(),
        )
    } else {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(size.dp).fillMaxWidth(),
            iterations = 100,
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun CustomPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    scale: Boolean = false,
) {
    val CrossfadeDurationMs = 100

    val IndicatorSize = 40.dp
    val SpinnerShape = CircleShape
    val Elevation = 6.dp

    val showElevation by remember(refreshing, state) {
        derivedStateOf { refreshing || state.progress > 0.5f }
    }

    Surface(
        modifier = modifier
            .size(IndicatorSize)
            .pullRefreshIndicatorTransform(state, scale),
        shape = SpinnerShape,
        color = backgroundColor,
        elevation = if (showElevation) Elevation else 0.dp,
    ) {
//        Crossfade(
//            targetState = refreshing,
//            animationSpec = tween(durationMillis = CrossfadeDurationMs),
//            label = "",
//        ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            LoadingView()
        }
//        }
    }
}

@ExperimentalAnimationApi
@Composable
fun NewsCardListView(
    news: List<ContentModel>,
    mainViewModel: MainViewModel,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    RoundedSurface {
        Box() {
            LazyColumn(state = listState) {
                itemsIndexed(news) { index, item ->
                    NewsView(item, mainViewModel = mainViewModel)
                    if (index % 10 == 0) {
                        AdMobCompose(
                            modifier = Modifier.height(125.dp).fillMaxWidth(),
                        )
                    }
                    if (index == news.lastIndex) {
                        this@LazyColumn.item {
                            Box(
                                Modifier.fillMaxWidth().height(32.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                LoadingView()
                            }
                        }
                        mainViewModel.fetchLatestNews()
                    }
                }
            }
            val showButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }
            AnimatedVisibility(
                visible = showButton,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
            ) {
                IconButton(onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
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
        }
    }
}

@ExperimentalAnimationApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NewsView(
    aNews: ContentModel,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        Modifier.height(125.dp).fillMaxWidth().clickable {
            expanded = !expanded
        },
    ) {
        Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (aNews.image != null) {
                Log.i("Thumbnail", "Thumbnail load success")
                Log.d("Thumbnail", "Thumbnail: " + aNews.image)
                Box(
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)),
                ) {
                    GlideImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = { aNews.image },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.FillWidth,
                            contentDescription = stringResource(id = R.string.main_thumbnail),
                        ),
                        loading = {
                            LoadingView(LOTTIE_IMG_VIEW_URL)
                        },
                        component = rememberImageComponent {
                            // shows a shimmering effect when loading an image.
                            +ShimmerPlugin(
                                baseColor = Color(0xFF424242),
                                highlightColor = Color(0xA3C2C2C2),
                            )
                            +CircularRevealPlugin(
                                duration = 350,
                            )
                        },
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
            Text(
                modifier = Modifier.fillMaxWidth().weight(4f),
                text = aNews.title,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
            )
        }

//        Row {
//            Text(text = "조회수: " +aNews.hit.toString(), style = MaterialTheme.typography.body1)
//            Spacer(modifier = Modifier.padding(2.dp))
//            Text(text = aNews.numComment.toString(), style = MaterialTheme.typography.body1)
//        }
//        Divider(Modifier.padding(top = 12.dp, bottom = 0.dp))
    }

    if (expanded) {
        val context = LocalContext.current

        CoroutineScope(Dispatchers.Main).launch {
            if (!mainViewModel.isTabletUi.value) {
                val intent = Intent(context, ContentActivity::class.java).apply {
                    putExtra("content", aNews)
                }
                startActivity(context, intent, null)
            } else {
                mainViewModel.changeAnews(aNews)
            }
        }
        expanded = !expanded
    }
}

/**
 * @author brainer
 * @param contentView is Composable parameter
 */
@Composable
fun RoundedSurface(contentView: @Composable () -> Unit = {}) {
    val backGroundColor = getBackgroundColor()
    Surface(
        modifier = Modifier.background(backGroundColor),
        shape = RoundedCornerShape(ROUNDED_VALUE.dp),
    ) {
        contentView()
    }
}

@Preview
@Composable
fun ConnectErrorView() {
    Box {
        Row(Modifier.align(Alignment.Center)) {
            Icon(
                painterResource(R.drawable.ic_baseline_oui_error_24),
                modifier = Modifier.align(Alignment.CenterVertically),
                contentDescription = stringResource(R.string.connect_faild),
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "인터넷이 연결돼있지 않아요.\n인터넷 연결을 확인해주세요.",
                textAlign = TextAlign.Center,
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RoundedSurfacePreview() {
    RoundedSurface {
        ContentView(
            dummies[0],
            ContentViewModel(Application()),
        )
    }
}
