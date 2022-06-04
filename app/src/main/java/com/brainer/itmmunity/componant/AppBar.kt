package com.brainer.itmmunity.componant

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brainer.itmmunity.viewmodel.MainViewModel
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

const val SCROLL_HEIGHT = 295f

/**
 * AppBar is reusable and app based Comopse
 * @author brainer
 * @param viewModel MainViewModel
 * @param contentView content parameter for Scaffold
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun AppBar(viewModel: MainViewModel = MainViewModel(), contentView: @Composable () -> Unit = {}) {
    val containerColor = if (isSystemInDarkTheme()) {
        Black
    } else {
        Color(245, 244, 244)
    }

    val titleString by viewModel.titleString.collectAsState()
    val nestedScrollViewState = rememberNestedScrollViewState()
//    val topBarState = nestedScrollViewState.offset
    val smallTopAppBarAlpha by animateFloatAsState(1f)

    VerticalNestedScrollView(
        state = nestedScrollViewState,
        header = {
            Box(
                modifier = Modifier.height(SCROLL_HEIGHT.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .graphicsLayer(alpha = smallTopAppBarAlpha)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ITmmunity",
                        fontSize = 32.sp,
                    )
                }
            }
        },
        content = {
            Column {
                SmallTopAppBar(
                    title = {
                        AnimatedContent(
                            targetState = titleString,
                            transitionSpec = {
                                if (titleString != "ITmmunity") {
                                    slideInVertically { height -> height } + fadeIn() with
                                            slideOutVertically { height -> -height } + fadeOut()
                                } else {
                                    slideInVertically { height -> -height } + fadeIn() with
                                            slideOutVertically { height -> height } + fadeOut()
                                }.using(
                                    SizeTransform(clip = false)
                                )
                            }) { targetTitle ->
                            Text(
                                text = targetTitle,
                                modifier = Modifier.graphicsLayer(alpha = smallTopAppBarAlpha)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search button")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                ) {
                    contentView()
                }
                BottomNavigation(modifier = Modifier.height(62.dp), backgroundColor = containerColor) {
    //                val navBackStackEntry by navController.currentBackStackEntryAsState()
    //                val currentDestination = navBackStackEntry?.destination
    //                BottomNavigationItem(selected = 0, onClick = { /*TODO*/ }) {
    //                    /*TODO*/
    //                }
                }
            }
        }
    )
}
