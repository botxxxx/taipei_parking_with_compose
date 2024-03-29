package com.example.parking.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.main.MainFragmentDirections
import com.example.parking.main.MainViewModel
import com.example.parking.utils.Loading

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val isLoading: Boolean by viewModel.isLoading

    SetState(viewModel)
    BasicsTheme(isLoading) { modifier ->
        ColumnEditText(modifier) { user, pwd ->
            viewModel.doLogin(LOGIN_001_Rq(user, pwd))
        }
    }
}

@Composable
private fun BasicsSurfaceView(shouldShowOnboard: MutableState<Boolean>, content: @Composable () -> Unit) {
    BasicsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = lightColorPalette.background
        ) {
            if (shouldShowOnboard.value) {
                OnboardScreen { shouldShowOnboard.value = false }
            } else {
                content()
            }
        }
    }
}

@Composable
private fun OnboardScreen(btnClick: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the " + context.getString(R.string.app_name) + "!", color = Color.Black)
        Button(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(vertical = 24.dp),
            colors = ButtonDefaults.buttonColors(contentColor = lightColorPalette.primary),
            onClick = btnClick,
        ) {
            Text("Continue", color = lightColorPalette.background)
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ColumnEditText(modifier: Modifier = Modifier, onClick: (String, String) -> Unit = { _, _ -> }) {
    var user by remember { mutableStateOf(TextFieldValue("")) }
    var pwd by remember { mutableStateOf(TextFieldValue("")) }
    BaseAppBar {
        Column(
            modifier = modifier.fillMaxSize(),
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
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(vertical = 24.dp),
                colors = ButtonDefaults.buttonColors(contentColor = lightColorPalette.primary),
                onClick = { onClick.invoke(user.text, pwd.text) }
            ) {
                Text("Login", color = lightColorPalette.background)
            }
        }
    }
}

@Composable
private fun SetState(viewModel: MainViewModel) {
    viewModel.apply {
        val context = LocalContext.current
        val navController = getNavController()
        userData.observeAsState().value?.let { login ->
            Loading.hide()
            navController.actionToEntry(login)
        }
        onFailure.observeAsState().value?.let {
            Loading.hide()
            OnError(msg = context.getString(R.string.common_login_failure)) { onFailure.postValue(null) }
        }
    }
}

private fun NavController.actionToEntry(result: LOGIN_001_Rs?) {
    navigate(MainFragmentDirections.goEntry(result))
}