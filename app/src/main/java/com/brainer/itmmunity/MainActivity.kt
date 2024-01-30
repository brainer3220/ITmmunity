package com.brainer.itmmunity

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.brainer.itmmunity.MainActivity.Companion.navController
import com.brainer.itmmunity.presentation.componant.AppBar
import com.brainer.itmmunity.presentation.componant.ConnectErrorView
import com.brainer.itmmunity.presentation.componant.CustomPullRefreshIndicator
import com.brainer.itmmunity.presentation.componant.NewsCardListView
import com.brainer.itmmunity.presentation.navigation.NavigationView
import com.brainer.itmmunity.presentation.viewmodel.BackGroundViewModel
import com.brainer.itmmunity.presentation.viewmodel.CONFIG_STR
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel
import com.brainer.itmmunity.presentation.viewmodel.LoadState
import com.brainer.itmmunity.presentation.viewmodel.MainViewModel
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.launch
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox

const val TABLET_UI_WIDTH = 480
const val FIREBASE_MINIMUM_FETCH_SEC = 3600L
const val ANIMATION_DURATION = 350
const val ANIMATION_INIT_OFFSET_Y = 800
const val ANIMATION_TARGET_OFFSET_Y = 5000

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    companion object {
        @OptIn(ExperimentalMaterialNavigationApi::class)
        lateinit var bottomSheetNavigator: BottomSheetNavigator
        lateinit var navController: NavHostController
    }

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val remoteConfig = Firebase.remoteConfig
        val configSettings =
            remoteConfigSettings {
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
            OneUITheme {
                bottomSheetNavigator = rememberBottomSheetNavigator()
                navController = rememberNavController(bottomSheetNavigator)
                // A surface container using the 'background' color from the theme
                Surface {
                    NavigationView(navController)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult")

//        if (requestCode == REQUEST_CODE) {
//            Log.d("MainActivity", "onActivityResult REQUEST_INITIAL_ROUTE")
//        }
    }
}

@Suppress("ktlint:standard:function-naming")
/**
 * @author brainer
 * @param viewModel MainViewModel
 * @param networkViewModel BackGroundViewModel
 */
@ExperimentalAnimationApi
@Composable
fun MainViewWithAppBar(
    viewModel: MainViewModel,
    networkViewModel: BackGroundViewModel = BackGroundViewModel(Application()),
) {
    AppBar(viewModel = viewModel) {
        MainView(
            viewModel = viewModel,
            networkViewModel = networkViewModel,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalAnimationApi
@Composable
private fun MainView(
    viewModel: MainViewModel,
    networkViewModel: BackGroundViewModel,
) {
    val scope = rememberCoroutineScope()
//    val unifiedList by viewModel.unifiedList.collectAsStateWithLifecycle()
    val newsList by viewModel.newsList.collectAsStateWithLifecycle()
    val aNews by viewModel.aNews.collectAsStateWithLifecycle()
    val loadState by viewModel.loadState.collectAsStateWithLifecycle()
    val refreshing =
        remember {
            mutableStateOf(loadState == LoadState.LOADING)
        }

    val isConnection by networkViewModel.isConnect.collectAsStateWithLifecycle()
//    val isTabletUi by viewModel.isTabletUi.collectAsStateWithLifecycle()

    LaunchedEffect(loadState) {
        refreshing.value = loadState == LoadState.LOADING
    }

    fun refresh() =
        scope.launch {
            viewModel.refresh()
        }

    val pullRefreshState = rememberPullRefreshState(refreshing.value, ::refresh)

    Log.d("Unified_List", newsList.toString())

    if (isConnection) {
        BoxWithConstraints(
            Modifier.fillMaxSize(),
        ) {
            val boxWithConstraintsScope = this
            Row(Modifier.fillMaxSize()) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .pullRefresh(pullRefreshState),
                ) {
                    AnimatedContent(newsList.isNotEmpty(), label = "", transitionSpec = {
                        ContentTransform(
                            targetContentEnter = fadeIn(animationSpec = tween(500)),
                            initialContentExit = fadeOut(animationSpec = tween(500)),
                        )
                    }) { isNotEmpty ->
                        when (isNotEmpty) {
                            true -> {
                                NewsCardListView(
                                    newsList,
                                    viewModel,
                                )
                            }

                            false -> {
                                Column {
                                    repeat(10) {
                                        Row(
                                            modifier =
                                                Modifier.fillMaxWidth().height(100.dp)
                                                    .padding(16.dp),
                                        ) {
                                            RoundedCornerBox(
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize()
                                                        .weight(2f)
                                                        .placeholder(
                                                            visible = true,
                                                            highlight = PlaceholderHighlight.shimmer(),
                                                        ),
                                            ) {}
                                            Spacer(modifier = Modifier.width(16.dp))
                                            RoundedCornerBox(
                                                modifier =
                                                    Modifier
                                                        .fillMaxSize()
                                                        .weight(9f)
                                                        .placeholder(
                                                            visible = true,
                                                            highlight = PlaceholderHighlight.shimmer(),
                                                        ),
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (boxWithConstraintsScope.maxWidth >= TABLET_UI_WIDTH.dp) {
                    viewModel.changeTabletUi(true)
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                    ) {
                        Crossfade(targetState = aNews, label = "") {
                            if (it != null) {
                                ContentView(
                                    aNews = aNews!!,
                                    ContentViewModel(Application()),
                                    navController,
                                )
                            } else {
                                RoundedCornerBox {
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
            CustomPullRefreshIndicator(
                refreshing.value,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
            )
        }
    } else {
        RoundedCornerBox(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            ConnectErrorView()
        }
    }
}
