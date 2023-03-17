package com.example.parking.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.parking.api.data.TimeZone
import com.example.parking.callback.ChooseTimeZoneHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryFragment : Fragment(), ChooseTimeZoneHandler {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                EntryScreen()
            }
        }
    }

    override fun getPhone(): String {
        return "未設定電話"
    }

    override fun onTimeZoneChange(): (TimeZone) -> Unit {
        return { _ ->
//            val sessionToken = args.login?.sessionToken
//            val objectId = args.login?.objectId
//            val updateAt = args.login?.updatedAt
//            val update = UPDATE_001_Rq(sessionToken, objectId, null, updateAt)
//            viewModel.updateUser(update)
        }
    }
}