@file:OptIn(ExperimentalMaterial3Api::class)

package com.brainer.itmmunity.presentation.componant

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brainer.itmmunity.R
import com.brainer.itmmunity.domain.model.ContentModel
import com.brainer.itmmunity.presentation.viewmodel.MainViewModel
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

const val SCROLL_HEIGHT = 295f
const val SCROLL_TABLET_HEIGHT = 155f

@Suppress("ktlint:standard:function-naming")
/**
 * AppBar is reusable and app based Comopse
 * @author brainer
 * @param viewModel MainViewModel
 * @param contentView content parameter for Scaffold
 */
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun AppBar(
    viewModel: MainViewModel = MainViewModel(Application()),
    contentView: @Composable () -> Unit = {},
) {
    val containerColor =
        if (isSystemInDarkTheme()) {
            Black
        } else {
            Color(245, 244, 244)
        }
    val titleString by viewModel.titleString.collectAsStateWithLifecycle()
    val nestedScrollViewState = rememberNestedScrollViewState()
    val smallTopAppBarAlpha by animateFloatAsState(1f, label = "smallTopAppBarAlpha")
    val isTabletUi by viewModel.isTabletUi.collectAsStateWithLifecycle()

    VerticalNestedScrollView(
        state = nestedScrollViewState,
        header = {
            Box(
                modifier =
                    Modifier
                        .height(isTabletUi.let { if (it) SCROLL_TABLET_HEIGHT.dp else SCROLL_HEIGHT.dp })
                        .background(containerColor),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    Modifier
                        .graphicsLayer(alpha = smallTopAppBarAlpha)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                TopAppBar(
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
                                    SizeTransform(clip = false),
                                )
                            },
                            label = "",
                        ) { targetTitle ->
                            Text(
                                text = targetTitle,
                                modifier = Modifier.graphicsLayer(alpha = smallTopAppBarAlpha),
                            )
                        }
                    },
                    actions = {
                        Row {
                            IconButton(
                                onClick = {
                                },
                                enabled = false,
                                modifier =
                                    Modifier.combinedClickable(
                                        onLongClick = { TODO() },
                                        onClick = { TODO() },
                                    ),
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_baseline_oui_search_24),
                                    contentDescription = stringResource(R.string.search),
                                )
                            }
                            ShowDropDown()
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor),
                )
                Box(
                    Modifier.fillMaxSize(),
                ) {
                    contentView()
                }
                BottomNavigation(
                    modifier = Modifier.height(62.dp),
                    backgroundColor = containerColor,
                ) {
                    //                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    //                    val currentDestination = navBackStackEntry?.destination
                    //                    BottomNavigationItem(selected = 0, onClick = { /*TODO*/ }) {
                    //                        /*TODO*/
                    //                    }
                }
            }
        },
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppBar(
    content: ContentModel,
    context: Context,
    contentView: @Composable () -> Unit = {},
) {
    val containerColor =
        if (isSystemInDarkTheme()) {
            Black
        } else {
            Color(245, 244, 244)
        }

    Column {
        TopAppBar(
            title = {
//                Text(
//                    text = "ITmmunity",
//                    fontSize = 32.sp,
//                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    val activity = (context as? Activity)
                    activity?.finish()
                }) {
                    Icon(
                        painterResource(R.drawable.ic_baseline_oui_keyboard_arrow_left_24),
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    val sendIntent: Intent =
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TITLE, content.title)
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Powered by ITmmunity",
                            )
                            putExtra(Intent.EXTRA_TEXT, content.url)
                            type = "text/plain"
                        }

                    val shareIntent =
                        Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(
                        painterResource(R.drawable.ic_baseline_oui_share_24),
                        contentDescription = stringResource(R.string.share),
                    )
                }
            },
            colors =
                topAppBarColors(
                    containerColor,
                ),
        )
        contentView()
    }
}

@Suppress("ktlint:standard:function-naming")
@ExperimentalAnimationApi
@Preview
@Composable
fun AppBarPreview() {
    AppBar(
        contentView = {
            Text("Hello")
        },
    )
}
