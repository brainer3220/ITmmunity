package com.brainer.itmmunity

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brainer.itmmunity.presentation.componant.*
import com.brainer.itmmunity.presentation.ui.theme.ITmmunity_AndroidTheme
import com.brainer.itmmunity.presentation.viewmodel.BackGroundViewModel
import com.brainer.itmmunity.presentation.viewmodel.CONFIG_STR
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel
import com.brainer.itmmunity.presentation.viewmodel.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

const val TABLET_UI_WIDTH = 480
const val FIREBASE_MINIMUM_FETCH_SEC = 3600L
const val ANIMATION_DURATION = 350
const val ANIMATION_INIT_OFFSET_Y = 800
const val ANIMATION_TARGET_OFFSET_Y = 5000

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel(Application())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = FIREBASE_MINIMUM_FETCH_SEC
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(CONFIG_STR, "MainActivity Config params updated: $updated")
                    Log.d(CONFIG_STR, "MainActivity Fetch and activate succeeded")
                } else {
                    Log.d(CONFIG_STR, "Fetch failed")
                }
            }

        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    MainCompose(
                        viewModel = viewModel,
                        networkViewModel = viewModel(BackGroundViewModel::class.java),
                    )
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult")

//        if (requestCode == REQUEST_CODE) {
//            Log.d("MainActivity", "onActivityResult REQUEST_INITIAL_ROUTE")
//        }
    }
}

/**
 * @author brainer
 * @param viewModel MainViewModel
 * @param networkViewModel BackGroundViewModel
 */
@ExperimentalAnimationApi
@Composable
fun MainCompose(
    viewModel: MainViewModel,
    networkViewModel: BackGroundViewModel = BackGroundViewModel(Application()),
) {
    AppBar(viewModel = viewModel) {
        MainView(
            viewModel = viewModel,
            networkViewModel = networkViewModel,
        )
//        NavGraph(navController, viewModel, networkViewModel)
    }
}

@ExperimentalAnimationApi
@Composable
fun MainView(
    viewModel: MainViewModel = remember { MainViewModel(Application()) },
    networkViewModel: BackGroundViewModel,
) {
    val unifiedList by viewModel.unifiedList.collectAsState()
    val aNews by viewModel.aNews.collectAsState()

    val isConnection by networkViewModel.isConnect.collectAsState()
//    val isTabletUi by viewModel.isTabletUi.collectAsState()

    val swipeRefreshState by remember { mutableStateOf(true) }

    Log.d("Unified_List", unifiedList.toString())

    if (isConnection) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val boxWithConstraintsScope = this
            Row(Modifier.fillMaxSize()) {
                SwipeRefresh(
                    modifier = Modifier.weight(1f),
                    state = rememberSwipeRefreshState(isRefreshing = !swipeRefreshState),
                    onRefresh = { viewModel.getRefresh() },
                ) {
                    if (unifiedList.isNotEmpty()) {
                        NewsCardListView(
                            unifiedList,
                            viewModel,
                        )
                    } else {
                        RoundedSurface {
                            Box(
                                modifier = Modifier.fillMaxSize().align(Alignment.CenterVertically),
                                contentAlignment = Alignment.Center,
                            ) {
                                LoadingView()
                            }
                        }
                    }
                }

                if (boxWithConstraintsScope.maxWidth >= TABLET_UI_WIDTH.dp) {
                    viewModel.changeTabletUi(true)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                    ) {
                        Crossfade(targetState = aNews) {
                            if (it != null) {
                                ContentView(aNews = aNews!!, ContentViewModel(Application()))
                            } else {
                                RoundedSurface {
                                    Text(
                                        modifier = Modifier.fillMaxSize(),
                                        text = "컨텐츠를 클릭해 보세요.",
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                } else {
                    viewModel.changeTabletUi(false)
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ConnectErrorView()
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun MainViewTest() {
    MainView(
        networkViewModel = BackGroundViewModel(Application()),
    )
}
