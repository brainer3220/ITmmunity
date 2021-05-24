package com.brainer.itmmunity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.ui.DattaTheme
import com.google.accompanist.glide.rememberGlidePainter
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
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

@Preview(name = "MainView", device = Devices.DEFAULT)
@Composable
fun MainView(underkgNews: ArrayList<Croll.Content>?) {
    val scaffoldState = rememberScaffoldState()
    Box(Modifier.fillMaxSize())
    {
        val scroll = rememberScrollState(0)
        Column {
//            AppBar()
//            Spacer(modifier = Modifier.padding(2.dp))
            if (underkgNews != null) {
                NewsCard(underkgNews)
            }
        }
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
//    onSelected: (Content) -> Unit
) {
    var backGroundUnitColor: Color = Color(255, 255, 255)
    if (isSystemInDarkTheme()) {
        backGroundUnitColor = Color(23, 23, 23)
    } else if (!isSystemInDarkTheme()) {
        backGroundUnitColor = Color(255, 255, 255)
    }

    Surface(shape = RoundedCornerShape(25.dp)) {
        LazyColumn {
            items(news) { item ->
                NewsListOf(item)
            }
        }
    }
}

@Composable
fun NewsListOf(aNews: Croll.Content, modifier: Modifier = Modifier) {
    Surface(
        Modifier
            .height(125.dp)
            .fillMaxWidth()
            .background(Color.White)
            .clickable { }) {
            Row(modifier = modifier.padding(16.dp)) {
                if (aNews.image != null) {
                    Log.i("Thumbnail", "Thumbnail load success")
                    Log.d("Thumbnail", "Thumbnail: " + aNews.image)
                    Image(
                        modifier = Modifier
                            .height(120.dp)
                            .width(90.dp)
                            .padding(2.dp),
                        painter = rememberGlidePainter(
                            request = aNews.image,
                        ),
                        contentDescription = stringResource(R.string.main_thumbnail),
                    )
                }
                Text(text = aNews.title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.padding(4.dp))
            }
//        Row {
//            Text(text = "조회수: " +aNews.hit.toString(), style = MaterialTheme.typography.body1)
//            Spacer(modifier = Modifier.padding(2.dp))
//            Text(text = aNews.numComment.toString(), style = MaterialTheme.typography.body1)
//        }
//        Divider(Modifier.padding(top = 12.dp, bottom = 0.dp))
    }
}
