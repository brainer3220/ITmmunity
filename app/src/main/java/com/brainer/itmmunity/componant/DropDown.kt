package com.brainer.itmmunity.componant

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.brainer.itmmunity.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    mainContent: @Composable () -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        mainContent()
        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onDismissRequest()
            },
            modifier = Modifier
                .animateContentSize()
                .clickable { /*TODO*/ }
        ) {
            Text(
                text = "DropdownMenu",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun DropdownMenuPreview() {
    var moreDropDownExpanded by remember { mutableStateOf(false) }
    DropdownMenu(
        moreDropDownExpanded,
        onExpandedChange = { moreDropDownExpanded = !moreDropDownExpanded },
        onDismissRequest = { moreDropDownExpanded = false }) {
        IconButton(
            onClick = {
                moreDropDownExpanded = !moreDropDownExpanded
            }
        ) {
            Icon(
                painterResource(R.drawable.ic_baseline_oui_more_24),
                contentDescription = stringResource(R.string.more)
            )
        }
    }
}
