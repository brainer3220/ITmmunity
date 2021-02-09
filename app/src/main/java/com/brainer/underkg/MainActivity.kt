package com.brainer.underkg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brainer.datta.ui.DattaTheme
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }
}

@Preview
@Composable
fun MainView() {
    val scaffoldState = rememberScaffoldState()
    DattaTheme {
        Box(Modifier.fillMaxSize())
        {
            val scroll = rememberScrollState(0f)
            Column() {
                AppBar()
                Spacer(modifier = Modifier.padding(6.dp))
                NewsCard(dummies)
            }
        }
    }
}

@Composable
fun FilMaxBackGround() {
    if (isSystemInDarkTheme()) {
        Surface(color = com.brainer.underkg.ui.theme.themeOFDarkPrimary) {
        }
    } else if(!isSystemInDarkTheme()) {
        Surface(color = com.brainer.underkg.ui.theme.Primary) {
        }
    }
}

@Preview
@Composable
fun AppBar() {
    val scaffoldState = rememberScaffoldState()
    ScrollableColumn() {
        Scaffold(
                modifier = Modifier
                        .height(259.71.dp)
                        .fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "News",
                                style = MaterialTheme.typography.h2,
                                textAlign = TextAlign.Center)
                    }
                },
                bodyContent = { }
        )
    }
}

@Composable
fun NewsCard(
        news: List<NewsStruct>
//        onSelected: (NewsStruct) -> Unit
) {
    var backGroundUnitColor: Color = Color(255, 255, 255)
    if (isSystemInDarkTheme()) {
        backGroundUnitColor = Color(23, 23, 23)
    } else if (!isSystemInDarkTheme()) {
        backGroundUnitColor = Color(255, 255, 255)
    }

    Surface(shape = RoundedCornerShape(25.dp)) {
        LazyColumnFor(items = news) {
            NewsListOf(aNews = it, Modifier.background(backGroundUnitColor))
        }
    }
}

@Composable
fun NewsListOf(aNews: NewsStruct, modifier: Modifier = Modifier) {
    Surface(
            Modifier
                    .height(125.dp)
                    .clickable() { }) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(text = aNews.title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.padding(4.dp))
            Row() {
                Text(text = aNews.hit.toString(), style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = aNews.numComment.toString(), style = MaterialTheme.typography.body1)
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Divider()
        }
    }
}