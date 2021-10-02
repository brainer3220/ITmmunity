package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.Croll.KGNewsContent
import com.brainer.itmmunity.Croll.MeecoNews
import com.brainer.itmmunity.ui.DattaTheme
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.coroutines.*

const val FIT_IMAGE_SCRIPT = "<style>img{display: inline;height: auto;max-width: 100%;}</style>"

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        var unified_list = ArrayList<Croll.Content>()
        super.onCreate(savedInstanceState)

        GlobalScope.launch {
            val underkgNews =
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    KGNewsContent().returnData()
                }
            val meecoNews =
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    MeecoNews().returnData()
                }
            Log.d("meecoNews", "$meecoNews")

            unified_list.addAll(underkgNews)
            unified_list.addAll(meecoNews.slice(3 until meecoNews.size))
//            underkgNews.addAll(meecoNews.slice(3 until meecoNews.size))

            setContent {
                DattaTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        MainView(unified_list)
                    }
                }
            }
        }
        setContent {
            DattaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView(null)
                }
            }
        }
    }
}

@Composable
fun MainView(underkgNews: ArrayList<Croll.Content>?) {
    val scaffoldState = rememberScaffoldState()

    if (underkgNews != null) {
        Box(Modifier.fillMaxSize())
        {
            val scroll = rememberScrollState(0)
            Column {
//            AppBar()
//            Spacer(modifier = Modifier.padding(2.dp))
                NewsCard(underkgNews)
            }
        }
    } else {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = 10.dp)) {
            LoadingView()
        }
    }
}

@Preview
@Composable
fun LoadingView() {
    Column() {
        Spacer(modifier = Modifier.weight(10f))
        Text(modifier = Modifier
            .fillMaxWidth()
            .weight(1f), text = "로딩중입니다... ", fontSize = 20.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.weight(10f))
    }
}

//@Composable
//fun FilMaxBackGround() {
//    if (isSystemInDarkTheme()) {
//        Surface(Modifier.fillMaxSize(), color = com.brainer.ITmmunity.ui.theme.themeOFDarkPrimary) {
//        }
//    } else if (!isSystemInDarkTheme()) {
//        Surface(Modifier.fillMaxSize(), color = com.brainer.ITmmunity.ui.theme.Primary) {
//        }
//    }
//}

@Composable
fun AppBar() {
    val scaffoldState = rememberScaffoldState()
    Log.i("Simple Log", "scaffoldState: $scaffoldState")
    Column {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = { Text("Drawer content") },
//            topBar = {
//                TopAppBar(
//                    title = { Text("Simple Scaffold Screen") },
//                    navigationIcon = {
//                        IconButton(
//                            onClick = {
////                                scope.launch { scaffoldState.drawerState.open() }
//                            }
//                        ) {
//                            Icon(Icons.Filled.Menu, contentDescription = "Localized description")
//                        }
//                    }
//                )
//            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Inc") },
                    onClick = { /* fab click handler */ }
                )
            },
            content = { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    items(100) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp)
//                                .background(colors[it % colors.size])
                        )
                    }
                }
            }
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NewsCard(
    news: List<Croll.Content>
) {
    var backGroundUnitColor: Color = Color(255, 255, 255)
    if (isSystemInDarkTheme()) {
        backGroundUnitColor = Color(23, 23, 23)
    } else if (!isSystemInDarkTheme()) {
        backGroundUnitColor = Color(255, 255, 255)
    }

    val listState = rememberLazyListState()
    Surface(shape = RoundedCornerShape(25.dp)) {
        LazyColumn(state = listState) {
            items(news) { item ->
                NewsListOf(item)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@DelicateCoroutinesApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsListOf(aNews: Croll.Content, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var contentHtml: String?

    Column() {
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
                    Image(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        painter = rememberGlidePainter(
                            request = aNews.image,
                        ),
                        contentDescription = stringResource(R.string.main_thumbnail),
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f), text = aNews.title, style = MaterialTheme.typography.h6
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
            val contentIntent = Intent(Intent(LocalContext.current, ContentView::class.java))

            contentIntent.putExtra("content", aNews)
            context.startActivity(contentIntent)
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
