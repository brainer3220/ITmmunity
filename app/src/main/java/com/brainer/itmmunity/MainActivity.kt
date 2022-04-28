package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.brainer.itmmunity.componant.AppBar
import com.brainer.itmmunity.componant.LoadingView
import com.brainer.itmmunity.componant.NewsCard
import com.brainer.itmmunity.ViewModel.BackGroundViewModel
import com.brainer.itmmunity.ViewModel.MainViewModel
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

const val TABLET_UI_WIDTH = 480
const val ANIMATION_DURATION = 700
const val ANIMATION_INIT_OFFSET_Y = 800
const val ANIMATION_TARGET_OFFSET_Y = 5000
lateinit var APPLICATION_CONTEXT: Context

class MainActivity : ComponentActivity() {
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

        APPLICATION_CONTEXT = applicationContext
        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    MainCompose(networkViewModel = BackGroundViewModel(APPLICATION_CONTEXT))
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun MainCompose(
    viewModel: MainViewModel = remember { MainViewModel() },
    networkViewModel: BackGroundViewModel = BackGroundViewModel(context = APPLICATION_CONTEXT)
) {
    val navController = rememberAnimatedNavController()

    AppBar(viewModel = viewModel) {
        AnimatedNavHost(navController = navController, startDestination = "MainList") {
            composable(route = "MainList",
                enterTransition = {
                    when (initialState.destination.route) {
                        "Red" ->
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(ANIMATION_DURATION)
                            )
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        "Blue" ->
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(ANIMATION_DURATION)
                            )
                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        "Blue" ->
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(ANIMATION_DURATION)
                            )
                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        "Blue" ->
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(ANIMATION_DURATION)
                            )
                        else -> null
                    }
                }) {
                MainView(
                    viewModel = viewModel,
                    networkViewModel = networkViewModel,
                    navController = navController
                )
            }

            composable(route = "ContentView",
                enterTransition = {
                    slideInVertically(initialOffsetY = { ANIMATION_INIT_OFFSET_Y }) + fadeIn()
                },
                exitTransition = {
                    slideOutVertically(targetOffsetY = { ANIMATION_TARGET_OFFSET_Y }) + fadeOut()
                },
                popEnterTransition = {
                    slideInVertically(initialOffsetY = { ANIMATION_INIT_OFFSET_Y }) + fadeIn()
                },
                popExitTransition = {
                    slideOutVertically(targetOffsetY = { ANIMATION_TARGET_OFFSET_Y }) + fadeOut()
                }) { ContentView(viewModel = viewModel) }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedCrossfadeTargetStateParameter")
@Composable
fun MainView(
    viewModel: MainViewModel = remember { MainViewModel() },
    networkViewModel: BackGroundViewModel,
    navController: NavHostController
) {
    val unifiedList by viewModel.unifiedList.observeAsState(arrayListOf())
    val aNews by viewModel.aNews.observeAsState()

    val isConnection by networkViewModel.isConnect.observeAsState(true)

    val swipeRefreshState by remember { mutableStateOf(true) }

    Log.d("Unified_List", unifiedList.toString())


    if (isConnection) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val boxWithConstraintsScope = this
            Row(Modifier.fillMaxSize()) {
                SwipeRefresh(
                    modifier = Modifier.weight(1f),
                    state = rememberSwipeRefreshState(isRefreshing = !swipeRefreshState),
                    onRefresh = { viewModel.getRefresh() }) {
                    if (unifiedList.isNotEmpty()) {
                        Column {
                            NewsCard(
                                unifiedList,
                                viewModel,
                                navController = navController
                            )
                        }
                    } else {
                        LoadingView()
                    }
                }

                if (boxWithConstraintsScope.maxWidth >= TABLET_UI_WIDTH.dp) {
                    viewModel.changeTabletUi(true)
                    Box(modifier = Modifier.weight(1f)) {
                        Crossfade(targetState = aNews) {
                            if (it != null) {
                                ContentView(viewModel = viewModel)
                            } else {
                                Text(
                                    modifier = Modifier.fillMaxSize(),
                                    text = "컨텐츠를 클릭해 보세요.",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    viewModel.changeTabletUi(false)
                }
            }
        }
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
