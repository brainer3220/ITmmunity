package com.brainer.itmmunity

import android.app.Application
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
import com.brainer.itmmunity.data.Croll.Croll
import com.brainer.itmmunity.presentation.componant.AppBar
import com.brainer.itmmunity.presentation.ui.theme.ITmmunity_AndroidTheme
import com.brainer.itmmunity.presentation.viewmodel.ContentViewModel

lateinit var content: Croll.Content

@ExperimentalAnimationApi
class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        content = intent.getParcelableExtra("content")!!

        setContent {
            ITmmunity_AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppBar(content = content, context = LocalContext.current) {
                        ContentView(content, ContentViewModel(Application()))
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
