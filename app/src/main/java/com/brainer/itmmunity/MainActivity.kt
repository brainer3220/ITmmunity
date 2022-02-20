package com.brainer.itmmunity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.brainer.itmmunity.Componant.NewsCard
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.ViewModel.MainViewModel
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.DelicateCoroutinesApi

const val FIT_IMAGE_SCRIPT = "<style>img{display: inline;height: auto;max-width: 100%;}</style>"

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView(viewModel: MainViewModel = MainViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val unifiedList by viewModel.unifiedList.observeAsState(arrayListOf())

    val swipeRefreshState by remember { mutableStateOf(true) }

    Log.d("Unified_List", unifiedList.toString())

    val textColor: Color = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

//    val scrollBehavior = remember(decayAnimationSpec) {
//        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
//    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("ITmmunity", color = textColor)
                },
//                navigationIcon = {
//                        IconButton(
//                            onClick = {
////                                scope.launch { scaffoldState.drawerState.open() }
//                            }
//                        ) {
//                            Icon(Icons.Filled.Menu, contentDescription = "Localized description")
//                        }
//                }
            )
        },
        content = {
            BoxWithConstraints(Modifier.fillMaxSize()) {
                val boxWithConstraintsScope = this
                Row(Modifier.fillMaxSize()) {
                    SwipeRefresh(
                        modifier = Modifier.weight(1f),
                        state = rememberSwipeRefreshState(isRefreshing = !swipeRefreshState),
                        onRefresh = { viewModel.getRefresh() }) {
                        Column {
                            NewsCard(unifiedList, viewModel)
                        }
                    }

                    if (boxWithConstraintsScope.maxWidth >= 480.dp) {
                        Box(modifier = Modifier.weight(1f)) {
                            ContentView(
                                aNews = Croll.Content(
                                    hit = 0,
                                    image = null,
                                    numComment = 100,
                                    title = "test",
                                    url = "https://www.naver.com/"
                                )
                            )
                        }
                    }
                }
            }
        })
}
