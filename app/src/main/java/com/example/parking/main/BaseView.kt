package com.example.parking.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parking.R

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun BaseAppBar(
    arrowBackOnClick: (() -> Unit)? = null,
    settingsOnClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val arrowBack: @Composable (() -> Unit)? = arrowBackOnClick?.let {
            {
                IconButton(content = {
                    Icon(Icons.Filled.ArrowBack, null)
                }, onClick = it)
            }
        }
        val setting: @Composable RowScope.() -> Unit = {
            settingsOnClick?.let {
                IconButton(content = {
                    Icon(Icons.Filled.Settings, null)
                }, onClick = it)
            }
        }
        TopAppBar(
            title = { Text(text = LocalContext.current.getString(R.string.app_name)) },
            backgroundColor = MaterialTheme.colors.primarySurface,
            navigationIcon = arrowBack,
            actions = setting,
            elevation = 4.dp,
        )
        content()
    }
}