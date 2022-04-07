package com.brainer.itmmunity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.brainer.itmmunity.Componant.LoadingView
import com.brainer.itmmunity.Componant.NewsCard
import com.brainer.itmmunity.ViewModel.BackGroundViewModel
import com.brainer.itmmunity.ViewModel.MainViewModel
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.DelicateCoroutinesApi

val TABLET_UI_WIDTH = 480.dp

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("Config", "MainActivity Config params updated: $updated")
                    Log.d("Config", "MainActivity Fetch and activate succeeded")
                } else {
                    Log.d("Config", "Fetch failed")
                }
            }

        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView(networkViewModel = BackGroundViewModel(applicationContext))
                }
            }
        }
    }
}

@Composable
fun MainView(viewModel: MainViewModel = MainViewModel(), networkViewModel: BackGroundViewModel) {
    val backHandlingEnabled by remember { mutableStateOf(true) }

    val scaffoldState = rememberScaffoldState()
    val unifiedList by viewModel.unifiedList.observeAsState(arrayListOf())
//    val contentViewModel = remember{ ContentViewModel() }
    val aNews by viewModel.aNews.observeAsState()


    val isConnection by networkViewModel.isConnect.observeAsState(true)

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

    if (isConnection) {
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
                            if (unifiedList.isNotEmpty()) {
                                Column {
                                    NewsCard(unifiedList, viewModel)
                                }
                            } else {
                                LoadingView()
                            }
                        }

                        if (boxWithConstraintsScope.maxWidth >= TABLET_UI_WIDTH) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (aNews != null) {
                                    ContentView(aNews!!, viewModel = viewModel)
                                } else {
                                    Text(
                                        modifier = Modifier.fillMaxSize(),
                                        text = "컨텐츠를 클릭해 보세요.",
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            if (aNews != null) {
                                BackHandler(backHandlingEnabled) {
                                    viewModel.changeAnews(null)
                                }
                                Box(modifier = Modifier.fillMaxSize()) {
                                    ContentView(aNews!!, viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            })
    } else {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "인터넷이 연결돼있지 않아요.\n인터넷 연결을 확인해주세요.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
