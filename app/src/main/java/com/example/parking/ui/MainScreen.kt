package com.example.parking.ui

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.main.MainFragment
import com.example.parking.main.MainFragmentDirections
import com.example.parking.main.MainViewModel
import com.example.parking.utils.ShowNormalAlert

@Composable
fun MainScreen(fragment: MainFragment) {
    val viewModel: MainViewModel = hiltViewModel()
    SetView(fragment, viewModel)
    BasicsSurfaceView {
        val rootView = LocalView.current
        ColumnEditText { user, pwd ->
            viewModel.getServiceStateList(LOGIN_001_Rq(), rootView)
        }
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnEditText(onClick: (String, String) -> Unit = { _, _ -> }) {
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

@Composable
private fun SetView(fragment: MainFragment, viewModel: MainViewModel) {
    viewModel.apply {
        userLiveData.observe(fragment.viewLifecycleOwner) {
            it?.let {
                navigateToEntry(it, fragment.findNavController())
                clearResponse()
            }
        }
        onFailureLiveData.observe(fragment.viewLifecycleOwner) {
            // TODO Composable
//            onApiError()
        }
    }
}

private fun navigateToEntry(result: LOGIN_001_Rs?, navController: NavController) {
    val direction = MainFragmentDirections.actionToEntry(result)
    navController.navigate(direction)
}

@Composable
private fun onApiError() {
    val context = LocalContext.current
    ShowNormalAlert(
        title = context.getString(R.string.common_text_error_msg),
        msg = context.getString(R.string.common_login_failure),
        rightText = context.getString(R.string.common_text_i_know_it),
    )
}