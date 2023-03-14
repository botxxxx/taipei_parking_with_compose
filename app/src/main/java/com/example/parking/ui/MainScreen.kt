package com.example.parking.ui

import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.parking.R

@Composable
fun MainScreen(onClick: (View) -> (String, String) -> Unit) {
    BasicsSurfaceView {
        val rootView = LocalView.current
        ColumnEditText(onClick.invoke(rootView))
    }
}

@Composable
fun BasicsSurfaceView(content: @Composable () -> Unit) {
    BasicsCodeLabTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LightColorScheme.onPrimary
        ) {
            var shouldShowOnboard by rememberSaveable { mutableStateOf(true) }
            if (shouldShowOnboard) {
                OnboardScreen { shouldShowOnboard = false }
            } else {
                content()
            }
        }
    }
}

@Composable
fun OnboardScreen(btnClick: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the " + context.getString(R.string.app_name) + "!", color = Color.Black)
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            onClick = btnClick,
        ) {
            Text("Continue")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnEditText(onClick: (String, String) -> Unit) {
    var user by remember { mutableStateOf(TextFieldValue("")) }
    var pwd by remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(5.dp),
            value = user,
            onValueChange = { user = it },
            label = { Text(text = "user") },
            singleLine = true,
        )
        OutlinedTextField(
            modifier = Modifier.padding(5.dp),
            value = pwd,
            onValueChange = { pwd = it },
            label = { Text(text = "pwd") },
            singleLine = true,
        )
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onClick.invoke(user.text, pwd.text) }
        ) {
            Text("Login")
        }
    }
}