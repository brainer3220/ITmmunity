package com.brainer.itmmunity.componant

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.brainer.itmmunity.ContentActivity
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.R
import com.brainer.itmmunity.viewmodel.MainViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val DEFAULT_LOTTIE_VIEW_URL = "https://assets2.lottiefiles.com/packages/lf20_wfsunjgd.json"
const val LOTTIE_IMG_VIEW_URL = "https://assets2.lottiefiles.com/packages/lf20_6odgh2c6.json"
const val DEFAULT_SPACE_WEIGHT = 5F
const val ROUNDED_VALUE = 26

@Preview
@Composable
fun LoadingView(
    lottieURL: String? = DEFAULT_LOTTIE_VIEW_URL,
    spaceWeight: Float = DEFAULT_SPACE_WEIGHT
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(lottieURL!!))

    RoundedSurface {
        Surface(Modifier.fillMaxSize()) {
            Column {
                if (spaceWeight != 0F) {
                    Spacer(modifier = Modifier.weight(spaceWeight))
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (composition == null) {
                        CircularProgressIndicator(
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        )
                    } else {
                        LottieAnimation(
                            composition, modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        )
                    }
                }
                if (spaceWeight != 0F) {
                    Spacer(modifier = Modifier.weight(spaceWeight))
                }
            }
        }
    }
}


@Composable
fun NewsCard(
    news: List<Croll.Content>,
    mainViewModel: MainViewModel
) {
    val listState = rememberLazyListState()
    RoundedSurface {
        LazyColumn(state = listState) {
            itemsIndexed(news) { index, item ->
                NewsListOf(item, mainViewModel = mainViewModel)
                if (index % 10 == 0) {
                    AdMobCompose(
                        modifier = Modifier
                            .height(125.dp)
                            .fillMaxWidth()
                    )
                } else if (index == news.lastIndex - 15) {
                    this@LazyColumn.item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                        ) {
                            LoadingView(spaceWeight = 0F)
                        }
                    }
                    mainViewModel.addData()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NewsListOf(
    aNews: Croll.Content,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        Modifier
            .height(125.dp)
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            }) {
        Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (aNews.image != null) {
                Log.i("Thumbnail", "Thumbnail load success")
                Log.d("Thumbnail", "Thumbnail: " + aNews.image)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    GlideImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = aNews.image,
                        contentScale = ContentScale.FillWidth,
                        loading = {
                            LoadingView(LOTTIE_IMG_VIEW_URL, spaceWeight = 0F)
                        },
                        contentDescription = stringResource(id = R.string.main_thumbnail),
                        circularReveal = CircularReveal(duration = 350),
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                text = aNews.title,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis
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
                kotlin.runCatching {
                    mainViewModel.changeAnews(aNews)
                }
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
    val backGroundColor = if (isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color(245, 244, 244)
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(backGroundColor)
    ) {
        Surface(shape = RoundedCornerShape(ROUNDED_VALUE.dp)) {
            Box(Modifier.padding(bottom = 8.dp)) {
                contentView()
            }
        }
    }
}
