package com.brainer.itmmunity.Componant

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.R
import com.brainer.itmmunity.ViewModel.MainViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Preview
@Composable
fun LoadingView() {
    Column {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NewsCard(
    news: List<Croll.Content>,
    mainViewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val listState = rememberLazyListState()
    Surface(shape = RoundedCornerShape(25.dp)) {
        LazyColumn(state = listState, contentPadding = paddingValues) {
            itemsIndexed(news) { index, item ->
                NewsListOf(item, mainViewModel = mainViewModel)
                if (index == news.lastIndex) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    ) {
                        LoadingView()
                    }
                    mainViewModel.addData()
                } else {
                    this@LazyColumn.item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled", "CoroutineCreationDuringComposition")
@DelicateCoroutinesApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsListOf(aNews: Croll.Content, mainViewModel: MainViewModel, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Surface(
            Modifier
                .height(125.dp)
                .fillMaxWidth()
                .background(Color.White)
                .clickable { expanded = !expanded }) {
            Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (aNews.image != null) {
                    Log.i("Thumbnail", "Thumbnail load success")
                    Log.d("Thumbnail", "Thumbnail: " + aNews.image)
                    Box(modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))) {
                        GlideImage(
                            modifier = Modifier.fillMaxSize(),
                            imageModel = aNews.image,
                            contentScale = ContentScale.FillWidth,
                            contentDescription = stringResource(id = R.string.main_thumbnail),
                            circularReveal = CircularReveal(duration = 350),
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f), text = aNews.title, style = MaterialTheme.typography.titleLarge
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
            CoroutineScope(Dispatchers.Main).launch {
                kotlin.runCatching {
                    mainViewModel.changeAnews(aNews)
                }
            }
            expanded = !expanded
        }
    }
}


//@Composable
//fun DrawUnderKGNews(doc: String?) {
//    if (doc != null) {
//        Text(modifier = Modifier
////        .clickable { expanded = !expanded }
//            .fillMaxWidth(), text = doc, textAlign = TextAlign.Center)
//    }
//}

@Composable
fun NewsItem(item: Croll.Content) {
    Text(text = item.url)
}
