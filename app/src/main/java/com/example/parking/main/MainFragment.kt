package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.parking.R
import com.example.parking.api.data.LOGIN_001_Rq
import com.example.parking.api.data.LOGIN_001_Rs
import com.example.parking.ui.MainScreen
import com.example.parking.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MainScreen { view ->
                    { user, pwd ->
                        viewModel.getServiceStateList(LOGIN_001_Rq(user, pwd), view)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setView()
    }

    private fun setView() {
        viewModel.apply {
            userLiveData.observe(viewLifecycleOwner) {
                val nav = findNavController()
                it?.let {
                    navigateToEntry(it, nav)
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
    }

    private fun navigateToEntry(result: LOGIN_001_Rs?, navController: NavController) {
        val direction = MainFragmentDirections.actionToEntry(result)
        navController.navigate(direction)
    }

    private fun onServiceAPIError() {
        DialogUtils.showNormalAlert(
            context = context,
            title = resources.getString(R.string.common_text_error_msg),
            msg = resources.getString(R.string.common_login_failure),
            rightButtonText = resources.getString(R.string.common_text_i_know_it),
        )
    }
}