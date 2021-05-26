package com.brainer.itmmunity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SettingsView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {
                DefaultPreview()
            }
        }
    }
}

@Composable
fun SettingsList() {

}

@Composable
fun SettingContent(title: String, description: String) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(88.86.dp)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .clickable {  }) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp))
            Text(modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, top = 2.dp), text = title, fontSize = 30.sp)
            Text(modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, bottom = 2.dp), text = description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SettingContent("Connections", "brainer@kakao.com")
}
