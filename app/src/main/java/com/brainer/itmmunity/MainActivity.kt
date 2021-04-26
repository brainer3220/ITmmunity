package com.brainer.itmmunity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brainer.itmmunity.ui.DattaTheme
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DattaTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
    }
}
@Preview
@Composable
fun MainView() {
    val scaffoldState = rememberScaffoldState()
    Box(Modifier.fillMaxSize())
    {
        val scroll = rememberScrollState(0)
        Column {
//            AppBar()
//            Spacer(modifier = Modifier.padding(2.dp))
            NewsCard(dummies)
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
    news: List<Content>
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
fun NewsListOf(aNews: Content, modifier: Modifier = Modifier) {
    Surface(
        Modifier
            .height(125.dp)
            .background(Color.White)
            .clickable { }) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(text = aNews.title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(text = aNews.hit.toString(), style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = aNews.numComment.toString(), style = MaterialTheme.typography.body1)
            }
//            Spacer(Modifier.padding(top = 12.dp, bottom = 0.dp))
            Divider(Modifier.padding(top = 12.dp, bottom = 0.dp))
        }
    }
}