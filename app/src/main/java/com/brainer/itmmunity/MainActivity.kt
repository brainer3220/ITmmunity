package com.brainer.itmmunity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.brainer.itmmunity.componant.AppBar
import com.brainer.itmmunity.componant.LoadingView
import com.brainer.itmmunity.componant.NewsCard
import com.brainer.itmmunity.componant.RoundedSurface
import com.brainer.itmmunity.navcontrol.NavGraph
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme
import com.brainer.itmmunity.viewmodel.BackGroundViewModel
import com.brainer.itmmunity.viewmodel.CONFIG_STR
import com.brainer.itmmunity.viewmodel.MainViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.DelicateCoroutinesApi

const val TABLET_UI_WIDTH = 480
const val FIREBASE_MINIMUM_FETCH_SEC = 3600L
const val ANIMATION_DURATION = 700
const val ANIMATION_INIT_OFFSET_Y = 800
const val ANIMATION_TARGET_OFFSET_Y = 5000
lateinit var APPLICATION_CONTEXT: Context


@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private val mainvViewModel = MainViewModel()

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

        APPLICATION_CONTEXT = applicationContext
        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    MainCompose(
                        viewModel = mainvViewModel,
                        networkViewModel = BackGroundViewModel(APPLICATION_CONTEXT)
                    )
                }
            }
        }
    }
}

/**
 * @author brainer
 * @param viewModel MainViewModel
 * @param networkViewModel BackGroundViewModel
 */
@ExperimentalAnimationApi
@Preview
@Composable
fun MainCompose(
    viewModel: MainViewModel = remember { MainViewModel() },
    networkViewModel: BackGroundViewModel = BackGroundViewModel(context = APPLICATION_CONTEXT)
) {
    val navController = rememberAnimatedNavController()

    AppBar(viewModel = viewModel) {
        NavGraph(navController, viewModel, networkViewModel)
    }
}


@ExperimentalAnimationApi
@Composable
fun MainView(
    viewModel: MainViewModel = remember { MainViewModel() },
    networkViewModel: BackGroundViewModel,
    navController: NavHostController
) {
    val unifiedList by viewModel.unifiedList.collectAsState()
    val aNewsState = MutableLiveData(viewModel.aNews.collectAsState())
    val aNews by rememberSaveable { aNewsState.value!! }

    val isConnection by networkViewModel.isConnect.collectAsState()
    val isTabletUi by viewModel.isTabletUi.collectAsState()

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
                                ContentView(aNews = aNews!!)
                            } else {
                                RoundedSurface {
                                    Text(
                                        modifier = Modifier.fillMaxSize(),
                                        text = "컨텐츠를 클릭해 보세요.",
                                        textAlign = TextAlign.Center
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

@ExperimentalAnimationApi
@Preview
@Composable
fun MainViewTest() {
    val navController = rememberAnimatedNavController()
    MainView(
        networkViewModel = BackGroundViewModel(APPLICATION_CONTEXT),
        navController = navController
    )
}
