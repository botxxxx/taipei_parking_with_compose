package com.example.parking.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.parking.callback.BaseViewInterface
import com.example.parking.utils.DialogUtils
import com.example.parking.utils.UiUtils
import com.example.parking.widget.TextInputLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<B : ViewBinding> : BottomSheetDialogFragment(), BaseViewInterface {

    companion object {
        private const val DIALOG_WIDTH_IN_DP = 350
        private const val EXPANDED_TOP_OFFSET_IN_DP = 0
    }

    private var activityHandler: Context? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingCallback().invoke(inflater, container)
        return _binding?.root
    }

    override fun onStart() {
        super.onStart()
        setDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityHandler = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.currentFocus?.clearFocus()
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onDetach() {
        super.onDetach()
        activityHandler = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getContext(): Context? {
        return super.getContext()
    }

    override fun getRootView(): View? = binding.root

    override fun getLifeCycleScope(): LifecycleCoroutineScope = viewLifecycleOwner.lifecycleScope

    abstract fun bindingCallback(): (LayoutInflater, ViewGroup?) -> B

    fun <H> getActivityHandler(type: Class<H>?): H? {
        if (type != null && type.isInstance(activityHandler)) {
            return activityHandler as H
        }
        return null
    }

    private fun setDialog() {
        (dialog as? BottomSheetDialog)?.run {
            behavior.apply {
                expandedOffset = UiUtils.dpToPx(EXPANDED_TOP_OFFSET_IN_DP)
                state = BottomSheetBehavior.STATE_EXPANDED
                isDraggable = false
            }
            window?.run {
                setLayout(UiUtils.dpToPx(DIALOG_WIDTH_IN_DP), resources.displayMetrics.heightPixels)
                setDimAmount(DialogUtils.OVERLAY_ALPHA)
            }
            (view?.rootView as ViewGroup).viewTreeObserver.addOnGlobalFocusChangeListener { viewLostFocus, viewGetFocus ->
                viewLostFocus.takeIf { viewGetFocus !is EditText && viewGetFocus !is TextInputLayout }?.let {
                    UiUtils.hideKeyboard(this.context, it)
                }
            }
        }
    }

    fun EditText.setEditorAction() {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus()
                return@setOnEditorActionListener true
            }
            false
        }
    }
}