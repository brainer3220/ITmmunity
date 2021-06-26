package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.Croll.KGNewsContent
import com.brainer.itmmunity.ui.DattaTheme
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.coroutines.*
import androidx.compose.foundation.isSystemInDarkTheme

val FIT_IMAGE_SCRIPT = "<style>img{display: inline;height: auto;max-width: 100%;}</style>"

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch  {
            val underkgNews = CoroutineScope(Dispatchers.Default).async {
                Croll().returnData()
            }.await()
            setContent {
                DattaTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        MainView(underkgNews)
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

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("SetJavaScriptEnabled")
@DelicateCoroutinesApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsListOf(aNews: Croll.Content, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var contentHtml: String = "로딩중..."

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
        AnimatedVisibility(visible = expanded) {
            runBlocking{
                CoroutineScope(Dispatchers.Default).async {
                    contentHtml = KGNewsContent().returnData(aNews.url).toString() + FIT_IMAGE_SCRIPT
                    Log.d("contentHTML", contentHtml)
                }.await()
            }

            val isDarkMode = isSystemInDarkTheme()

            AndroidView(modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clickable { expanded = !expanded },
                factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadData(contentHtml, "text/html", "utf-8")
                }
            }, update = {
                    it.settings.javaScriptEnabled = true
                    it.loadData(contentHtml, "text/html", "utf-8")

//                    it.getSettings().layoutAlgorithm = LayoutAlgorithm.SINGLE_COLUMN

                    it.settings.layoutAlgorithm = LayoutAlgorithm.SINGLE_COLUMN
                    it.isHorizontalScrollBarEnabled = false;

//                    it.isHorizontalScrollBarEnabled = false;

//                    it.settings.useWideViewPort = true
                    it.settings.loadWithOverviewMode = true

                if (isDarkMode) {
                    it.settings.forceDark = FORCE_DARK_ON
                } else {
                    it.settings.forceDark = FORCE_DARK_OFF
                }
            })

//            SelectionContainer {
//                Text(
//                    modifier = Modifier
//                        .clickable { expanded = !expanded }
//                        .fillMaxWidth()
//                        .padding(2.dp), text = contenHtml, textAlign = TextAlign.Center
//                )
//            }
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
