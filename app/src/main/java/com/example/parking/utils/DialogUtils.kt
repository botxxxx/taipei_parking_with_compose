package com.example.parking.utils

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import com.example.parking.R
import com.example.parking.databinding.NormalAlertDialogBinding
import com.example.parking.ui.BasicsCodeLabTheme
import com.example.parking.utils.DialogUtils.COMMON_BUTTON_ACTION
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtils {

    private const val NORMAL_DIALOG_WIDTH = 360
    const val OVERLAY_ALPHA = 0.408f

    private var dialog: AlertDialog? = null

    /**
     *  if only single btn set Text in neutralText.
     *  @param context Context
     *  @param iconRes res id default is R.drawable.ic_circle_call
     *  @param title alert title
     *  @param msg alert message
     *  @param rightButtonText right btn text
     *  @param rightButtonListener right btn click listener
     *  @param leftButtonText center btn text
     *  @param leftButtonListener center btn click listener
     * */
    fun showNormalAlert(
        context: Context?,
        iconRes: Int = R.drawable.ic_dialogs_bell,
        title: String? = "",
        msg: String? = "",
        rightButtonText: String? = null,
        rightButtonListener: View.OnClickListener? = null,
        leftButtonText: String? = null,
        leftButtonListener: View.OnClickListener? = null,
    ) {
        dialog?.apply {
            if (ownerActivity?.isFinishing == false) {
                dismiss()
            }
        }
        context?.let { ctx ->
            NormalAlertDialogBinding.inflate(LayoutInflater.from(ctx)).apply {
                ivIcon.setImageResource(iconRes)
                tvDialogTitle.text = title
                tvDialogMsg.text = msg
                val dialog = MaterialAlertDialogBuilder(ctx).setView(root).setCancelable(false).create().apply {
                    this@DialogUtils.dialog = this
                    window?.setBackgroundDrawableResource(R.color.transparent)
                    window?.setLayout(UiUtils.dpToPx(NORMAL_DIALOG_WIDTH), ViewGroup.LayoutParams.WRAP_CONTENT)
                    window?.setDimAmount(OVERLAY_ALPHA)
                }.also { it.show() }
                mbtnRightButton.apply {
                    text = rightButtonText
                    setOnClickListener {
                        rightButtonListener?.onClick(this)
                        closeDialog(dialog)
                    }
                }
                mbtnLeftButton.apply {
                    isVisible = !leftButtonText.isNullOrEmpty()
                    text = leftButtonText
                    setOnClickListener {
                        leftButtonListener?.onClick(this)
                        closeDialog(dialog)
                    }
                }
            }
        }
    }

    fun closeDialog() {
        dialog?.dismiss()
        dialog = null
    }

    private fun closeDialog(dialog: AlertDialog) {
        dialog.dismiss()
    }

    fun showDefaultNetworkAlert(
        context: Context?,
        title: String = COMMON_TITLE_ERROR,
        message: String = COMMON_DEFAULT_DESC_ERROR,
        rightButtonText: String = COMMON_BUTTON_ACTION,
        rightButtonListener: View.OnClickListener? = null,
    ) {
        showNormalAlert(
            context = context,
            title = title,
            msg = message,
            rightButtonText = rightButtonText,
            rightButtonListener = rightButtonListener
        )
    }

    const val COMMON_TITLE_ERROR = "網路連線品質不佳"
    const val COMMON_BUTTON_ACTION = "我知道了"
    const val COMMON_DEFAULT_DESC_ERROR = "若嘗試後沒有改善，請洽行員協助。\n[DBS-9999]"
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ShowNormalAlert(
    iconRes: Int = R.drawable.ic_dialogs_bell,
    title: String = "提示",
    msg: String = "",
    leftText: String = "",
    leftClick: () -> Unit = {},
    rightText: String = COMMON_BUTTON_ACTION,
    rightClick: () -> Unit = {},
) {
    BasicsCodeLabTheme {
        var dialogOpen by remember { mutableStateOf(true) }
        val onDismiss = { dialogOpen = false }
        if (dialogOpen) {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title
                    )
                },
                text = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = msg
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val leftTextEmpty = leftText.isEmpty()
                        val leftTextAlpha = if (leftTextEmpty) 0f else 1f
                        GradientButton(
                            modifier = Modifier.alpha(leftTextAlpha),
                            text = leftText,
                            onClick = {
                                if (leftTextEmpty)
                                    return@GradientButton
                                leftClick()
                                onDismiss()
                            }
                        )
                        GradientButton(
                            modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
                            text = rightText,
                            onClick = {
                                rightClick()
                                onDismiss()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(5.dp),
                backgroundColor = Color.White
            )
        }
    }
}

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    gradient: Brush = Brush.horizontalGradient(listOf(Color(0xFF72C361), Color(0xFF4FB980))),
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
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 12.dp, vertical = 9.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = Color.White
            )
        }
    }
}