package com.brainer.itmmunity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.brainer.itmmunity.Croll.Croll
import com.brainer.itmmunity.componant.AdMobCompose
import com.brainer.itmmunity.componant.AppBar
import com.brainer.itmmunity.ui.theme.ITmmunity_AndroidTheme

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
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppBar(content = content, context = LocalContext.current) {
                        Column {
                            ContentView(content)

                            AdMobCompose(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                adId = "ca-app-pub-1000428004132415/8571464204"
                            )
                        }
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
