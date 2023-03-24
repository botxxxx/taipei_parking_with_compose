package com.example.parking.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.parking.R

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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ShowNormalAlert(
    iconRes: Int = R.drawable.ic_dialogs_bell,
    title: String = "提示",
    msg: String = "",
    leftText: String = "",
    leftClick: () -> Unit = {},
    rightText: String = LocalContext.current.getString(R.string.common_text_i_know_it),
    rightClick: () -> Unit = {},
) {
    BasicsCodeLabTheme {
        var dialogOpen by remember { mutableStateOf(true) }
        val onDismiss = { dialogOpen = false }
        if (dialogOpen) {
            AlertDialog(modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
                shape = RoundedCornerShape(5.dp),
                backgroundColor = Color.White,
                onDismissRequest = {},
                title = { Text(modifier = Modifier.fillMaxWidth(), text = title) },
                text = { Text(modifier = Modifier.fillMaxWidth(), text = msg) },
                buttons = {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .padding(horizontal = 24.dp)
                    ) {
                        val (left, right) = createRefs()
                        if (leftText.isNotEmpty()) {
                            GradientButton(modifier = Modifier.constrainAs(left) { start.linkTo(parent.start);top.linkTo(parent.top) }, text = leftText, onClick = {
                                leftClick()
                                onDismiss()
                            })
                        }
                        val gradient: Brush = Brush.horizontalGradient(listOf(Color(0xFF72C361), Color(0xFF4FB980)))
                        GradientButton(modifier = Modifier.constrainAs(right) { end.linkTo(parent.end); top.linkTo(parent.top) },
                            boxModifier = Modifier.background(gradient),
                            text = rightText,
                            onClick = {
                                rightClick()
                                onDismiss()
                            })
                    }
                })
        }
    }
}

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    boxModifier: Modifier = Modifier.background(Color.White),
    text: String,
    onClick: () -> Unit = { },
    roundedShape: Int = 8,
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() },
        shape = RoundedCornerShape(roundedShape.dp),
    ) {
        Box(
            modifier = boxModifier
                .padding(horizontal = 12.dp, vertical = 9.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, color = Color.White)
        }
    }
}