package com.brainer.itmmunity.componant

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.brainer.itmmunity.R
import com.brainer.itmmunity.SettingActivity

@Preview
@Composable
fun ShowDropDown() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painterResource(R.drawable.ic_baseline_oui_more_24),
                contentDescription = "Settings"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.animateContentSize()
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    val intent = Intent(context, SettingActivity::class.java)
                    startActivity(
                        context,
                        intent,
                        null
                    )
                },
                text = {
                    Text("Settings", textAlign = TextAlign.Center)
                }
            )
        }
    }
}
