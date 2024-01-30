package com.brainer.itmmunity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.brainer.itmmunity.domain.model.ContentModel
import com.brainer.itmmunity.presentation.componant.AppBar
import com.brainer.itmmunity.presentation.navigation.ContentNavigationView
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import org.oneui.compose.theme.OneUITheme

lateinit var content: ContentModel

@ExperimentalAnimationApi
class ContentActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        content = intent.getParcelableExtra("content")!!

        setContent {
            val navController = rememberNavController()
            OneUITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppBar(content = content, context = LocalContext.current) {
                        ContentNavigationView(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("content", content)
        setResult(RESULT_OK, intent)
    }
}
