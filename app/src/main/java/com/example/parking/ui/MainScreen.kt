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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.findNavController
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.main.MainFragmentDirections
import com.example.parking.main.MainViewModel
import com.example.parking.utils.Loading

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = hiltViewModel()
    val rootView = LocalView.current
    val shouldShowOnboard = rememberSaveable { mutableStateOf(true) }
    SetState(viewModel)
    BasicsSurfaceView(shouldShowOnboard) {
        ColumnEditText { user, pwd ->
            viewModel.getLogin(LOGIN_001_Rq(user, pwd), rootView)
        }
    }
}

@Composable
private fun BasicsSurfaceView(shouldShowOnboard: MutableState<Boolean>, content: @Composable () -> Unit) {
    BasicsCodeLabTheme {
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
private fun ColumnEditText(onClick: (String, String) -> Unit = { _, _ -> }) {
    var user by remember { mutableStateOf(TextFieldValue("")) }
    var pwd by remember { mutableStateOf(TextFieldValue("")) }
    BaseAppBar {
        Column(
            modifier = Modifier.fillMaxSize(),
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
        userData.observeAsState().value?.let {
            Loading.hide()
            NavigateToEntry(it)
        }
        val context = LocalContext.current
        onFailure.observeAsState().value?.let {
            Loading.hide()
            OnError(msg = context.getString(R.string.common_login_failure)) { onFailure.postValue(null) }
        }
    }
}

@Composable
private fun NavigateToEntry(result: LOGIN_001_Rs?) {
    val direction = MainFragmentDirections.actionToEntry(result)
    val navController = LocalView.current.findNavController()
    navController.navigate(direction)
}

@Composable
fun OnError(msg: String, rightClick: () -> Unit) {
    val context = LocalContext.current
    ShowNormalAlert(
        title = context.getString(R.string.common_text_error_msg),
        msg = msg,
        rightText = context.getString(R.string.common_text_i_know_it),
        rightClick = rightClick
    )
}