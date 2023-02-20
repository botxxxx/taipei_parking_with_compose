package com.example.parking.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.parking.callback.BaseViewInterface

abstract class BaseFragment : Fragment(), BaseViewInterface {

    private var activityHandler: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityHandler = context
    }

    override fun onPause() {
        view?.findFocus()?.clearFocus()
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        activityHandler = null
    }

    fun <H> getActivityHandler(type: Class<H>?): H? {
        if (type != null && type.isInstance(activityHandler)) {
            return activityHandler as H
        }
        return null
    }

    override fun getContext(): Context? {
        return super.getContext()
    }

    override fun getLifeCycleScope(): LifecycleCoroutineScope {
        return viewLifecycleOwner.lifecycleScope
    }
}