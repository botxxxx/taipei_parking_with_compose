package com.example.parking.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.databinding.FragmentMainBinding
import com.example.parking.fragment.BaseViewBindingFragment
import com.example.parking.ui.BasicsCodeLabTheme
import com.example.parking.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseViewBindingFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setView()
    }

    private fun setView() {
        binding.cvView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DefaultPreview()
            }
        }
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    navigateToEntry(it, binding.root)
                    clearResponse()
                }
            }
            onFailureLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    onServiceAPIError()
                    clearResponse()
                }
            }
        }
//        binding.apply {
//            mbtnLogin.setOnClickListener {
//                viewModel.getServiceStateList(LOGIN_001_Rq(tilUser.text, tilPwd.text), this@MainFragment)
//            }
//            tilPwd.setEndIconOnClickListener(object : TextInputLayout.EndIconOnClickCallback {
//                override fun onClick(til: TextInputLayout, view: View) {
//                    val inputTypeStateSwitchTo = when (til.getInputType()) {
//                        TextInputLayout.INPUT_TYPE_TEXT_PASSWORD_MASK -> {
//                            Pair(TextInputLayout.INPUT_TYPE_TEXT_PASSWORD, R.drawable.ic_eye_open_apple)
//                        }
//                        else -> Pair(TextInputLayout.INPUT_TYPE_TEXT_PASSWORD_MASK, R.drawable.ic_eye_close_apple)
//                    }
//                    val inputType = til.getTypeface() //保持隱碼為全型，避免被覆蓋
//                    til.setInputType(inputTypeStateSwitchTo.first)
//                    til.setCustomIcon(inputTypeStateSwitchTo.second)
//                    til.putCursorToTextTail()
//                    til.setTypeface(inputType)
//                }
//            })
//        }
    }

    private fun navigateToEntry(result: LOGIN_001_Rs?, view: View) {
        val direction = MainFragmentDirections.actionToEntry(result)
        view.findNavController().navigate(direction)
    }

    private fun onServiceAPIError() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_error_msg),
            msg = resources.getString(R.string.common_login_failure),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }

    @Preview(showBackground = true, widthDp = 320, uiMode = Configuration.UI_MODE_NIGHT_NO)
    @Composable
    fun DefaultPreview() {
        BasicsCodeLabTheme {
            ColumnEditText { user, pwd ->
                viewModel.getServiceStateList(LOGIN_001_Rq(user, pwd), this@MainFragment)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ColumnEditText(loginOnClick: (String, String) -> Unit) {
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
                onClick = { loginOnClick.invoke(user.text, pwd.text) }
            ) {
                Text("Login")
            }
        }
    }

    @Composable
    fun BasicsSurfaceView(content: @Composable () -> Unit) {
        BasicsCodeLabTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorScheme.background
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
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the " + getString(R.string.app_name) + "!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                colors = buttonColors(containerColor = colorScheme.primary),
                onClick = btnClick,
            ) {
                Text("Continue")
            }
        }
    }

    override fun bindingCallback(): (LayoutInflater, ViewGroup?) -> FragmentMainBinding = { layoutInflater, viewGroup ->
        FragmentMainBinding.inflate(layoutInflater, viewGroup, false)
    }
}