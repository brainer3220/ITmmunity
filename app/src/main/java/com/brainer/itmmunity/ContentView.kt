package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.Componant.LoadingView
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.ViewModel.ContentViewModel
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContentView : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ContentViewModel()
        val aNews = intent.getParcelableExtra<Croll.Content>("content")

        CoroutineScope(Dispatchers.Main).launch {
            setContent {
                if (aNews != null) {
//                    CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getHtml(aNews)
//                    }
                }

                ITmmunity_AndroidTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        if (aNews != null) {
                            ContentView(aNews, viewModel)
                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ContentView(aNews: Croll.Content, viewModel: ContentViewModel) {
    val isDarkMode = isSystemInDarkTheme()
    val contentHtml by viewModel.contentHtml.observeAsState()
    val listState = rememberScrollState()

    if (contentHtml == null) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp)
        ) {
            LoadingView()
        }
    } else {
        Column {
            Row {
                TopAppBar(title = {
                    Text(
                        text = aNews.title,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                    actions = {
                        val context = LocalContext.current
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "공유",
                            Modifier.clickable {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TITLE, aNews.title.toString())
                                    putExtra(Intent.EXTRA_SUBJECT, "Powered by ITmmunity")
                                    putExtra(Intent.EXTRA_TEXT, aNews.url)
                                    type = "text/plain"
                                }

                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            })
                    })
                Spacer(modifier = Modifier.padding(4.dp))
            }

            SelectionContainer {
                Column(Modifier.verticalScroll(listState)) {
                    MarkdownText(modifier = Modifier.padding(8.dp), markdown = contentHtml!!)
                }
            }
        }
    }
}
