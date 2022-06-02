package com.brainer.itmmunity.componant

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.brainer.itmmunity.viewmodel.MainViewModel


/**
 * AppBar is reusable and app based Comopse
 * @author brainer
 * @param viewModel MainViewModel
 * @param contentView content parameter for Scaffold
 */
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Preview
@Composable
fun AppBar(viewModel: MainViewModel = MainViewModel(), contentView: @Composable () -> Unit = {}) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val titleString by viewModel.titleString.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    AnimatedContent(targetState = titleString,
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
                        Text(targetTitle)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = "Search button")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
//        bottomBar = {
//            BottomNavigation() {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = navBackStackEntry?.destination
//                BottomNavigationItem(selected = 0, onClick = { /*TODO*/ }) {
//                    /*TODO*/
//                }
//            }
//        },
        content = {
            Box(Modifier.fillMaxSize().padding(it)) {
                contentView()
            }
        })
}
