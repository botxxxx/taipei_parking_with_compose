package com.example.parking.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.parking.R
import com.example.parking.databinding.TextInputLayoutBinding
import com.example.parking.utils.UiUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 *     sample:
 *     on xml <com.cathaybk.common.widget.DBSTextInputLayout/>
 *     dtil3.setText("U225738146")
 *     dtil4.setError("輸入格式錯誤", R.drawable.ic_camera, R.color.pale_sky)
 *     dtil5.setError("此為必填欄位", R.drawable.ic_circle_error_filled, R.color.transparent)
 *     dtil6.isEnabled = false
 *     dtil7.setError("驗證尚未完成，請確認信箱輸入正確並再試一次", R.drawable.ic_circle_error_filled, R.color.red_orange)
 *     dtil8.setText("電子郵件")
 *     dtil8.setVerifySuccessState("電子郵件",R.drawable.ic_circle_success_filled, R.color.apple)
 */
class TextInputLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: TextInputLayoutBinding

    // callback
    private var onFocusChangeCallback: OnFocusChangeListener? = null
    private var onClickCallback: OnClickListener? = null
    private var onEditorActionCallback: TextView.OnEditorActionListener? = null
    private var onKeyCallback: OnKeyListener? = null
    private var endIconOnClickCallback: EndIconOnClickCallback? = null
    // private var onTouchCallback: OnTouchListener? = null

    // filter
    private val inputFilterSet: MutableSet<InputFilter> = mutableSetOf()

    private val textWatcherList: ArrayList<TextWatcher> = ArrayList()

    var text: String
        get() = binding.tietEditText.text?.toString()?.trim() ?: ""
        set(text) {
            binding.run {
                tietEditText.setText(text.trim())
                tilLayout.hint = getCurrentHintText(false)
            }
        }

    @JvmName("text") // 這是為了讓既有的code不要修改而新增
    fun setText(text: String?) {
        binding.tietEditText.setText(text)
    }

    var hint: String = ""
        set(text) {
            field = text
            binding.tilLayout.hint = getCurrentHintText(false)
        }

    var floatingTitle: String = ""
        set(text) {
            field = text
            binding.tilLayout.hint = getCurrentHintText(false)
        }

    var helperText: String
        get() = binding.tilLayout.helperText?.toString()?.trim() ?: ""
        set(helperText) {
            binding.tilLayout.helperText = helperText
            if (helperText.isNotBlank()) {
                clearError()
            } else {
                setError(binding.tilLayout.error?.toString() ?: "")
            }
        }

    var filters: Array<InputFilter>
        get() = binding.tietEditText.filters
        private set(inputFilter) {
            binding.tietEditText.filters = inputFilter
        }

    init {
        initView(context)
        initListener()

        context.theme.obtainStyledAttributes(attrs, R.styleable.DBSTextInputLayout, 0, 0).apply {
            initViewFromXml(context)
        }.recycle()
    }

    private fun TypedArray.initViewFromXml(context: Context) {
        text = getString(R.styleable.DBSTextInputLayout_android_text) ?: ""
        // hint and floating title
        hint = getString(R.styleable.DBSTextInputLayout_android_hint) ?: ""
        floatingTitle = getString(R.styleable.DBSTextInputLayout_floatingTitle) ?: ""
        // digits
        val digits = getString(R.styleable.DBSTextInputLayout_android_digits) ?: ""
        if (digits.isNotBlank()) {
            binding.tietEditText.run {
                keyListener = DigitsKeyListener.getInstance(digits)
                inputType = InputType.TYPE_CLASS_TEXT
            }
        }
        // max length
        val maxLength = getInteger(R.styleable.DBSTextInputLayout_android_maxLength, DEFAULT_TIET_MAX_LENGTH)
        addInputFilters(InputFilter.LengthFilter(maxLength))
        // input type
        setInputType(getInteger(R.styleable.DBSTextInputLayout_android_inputType, DEFAULT_TIET_INPUT_TYPE))
        // input method enter options
        binding.tietEditText.imeOptions = getInteger(R.styleable.DBSTextInputLayout_android_imeOptions, EditorInfo.IME_ACTION_DONE)
        // end icon - normal
        val endIconResId: Int = getResourceId(R.styleable.DBSTextInputLayout_endIconDrawable, DEFAULT_RES_ID)
        if (endIconResId != DEFAULT_RES_ID) {
            binding.tilLayout.endIconDrawable = getEndIconDrawable(endIconResId)
        }
        val endIconTint: Int = getColor(R.styleable.DBSTextInputLayout_endIconTint, DEFAULT_COLOR)
        if (endIconTint != DEFAULT_COLOR) {
            binding.tilLayout.setEndIconTintList(ColorStateList.valueOf(endIconTint))
        }
        // end icon - error
        val errorIconResId: Int = getResourceId(R.styleable.DBSTextInputLayout_errorIconDrawable, DEFAULT_RES_ID)
        if (errorIconResId != DEFAULT_RES_ID) {
            binding.tilLayout.errorIconDrawable = ContextCompat.getDrawable(context, errorIconResId)
        }
        val errorIconTint: Int = getColor(R.styleable.DBSTextInputLayout_errorIconTint, DEFAULT_COLOR)
        if (errorIconTint != DEFAULT_COLOR) {
            binding.tilLayout.setErrorIconTintList(ColorStateList.valueOf(errorIconTint))
        }
        // hint text
        binding.tilLayout.hint = getCurrentHintText(false)
        // helper text
        val helperText: String = getString(R.styleable.DBSTextInputLayout_helperText) ?: ""
        binding.tilLayout.helperText = helperText
        // clickable
        binding.tietEditText.isClickable = getBoolean(R.styleable.DBSTextInputLayout_android_clickable, false)
        // focusable
        binding.tietEditText.isFocusable = getBoolean(R.styleable.DBSTextInputLayout_focusable, true)
        // enabled
        isEnabled = getBoolean(R.styleable.DBSTextInputLayout_android_enabled, true)
        // to drop down list mode
        if (getBoolean(R.styleable.DBSTextInputLayout_toDropDownListMode, false)) {
            toDropDownListMode()
        }
    }

    private fun initView(context: Context) {
        inflate(context, R.layout.text_input_layout, this)
        binding = TextInputLayoutBinding.bind(this)
    }

    private fun initListener() {
        binding.tietEditText.apply {
            setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    false
                }
                onKeyCallback?.onKey(view, keyCode, keyEvent) ?: false
            }
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(view: TextView, actionId: Int, event: KeyEvent?): Boolean {
                    val isEventConsumed: Boolean = onEditorActionCallback?.onEditorAction(view, actionId, event) ?: false
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        view.clearFocus()
                        return isEventConsumed
                    }
                    return isEventConsumed
                }
            })
            setOnKeyListener { view: View, keyCode: Int, keyEvent: KeyEvent ->
                onKeyCallback?.onKey(view, keyCode, keyEvent) ?: false
            }
            setOnFocusChangeListener { view, hasFocus ->
                view as TextInputEditText
                binding.tilLayout.hint = getCurrentHintText(hasFocus)
                onFocusChangeCallback?.onFocusChange(view, hasFocus)
            }
            setOnClickListener {
                onClickCallback?.onClick(it)
            }
        }
    }

    fun setOnKeyCallback(onKeyCallback: OnKeyListener) {
        this.onKeyCallback = onKeyCallback
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickCallback = onClickListener
    }

    fun setFocusChangeCallback(onFocusChangeCallback: OnFocusChangeListener) {
        this.onFocusChangeCallback = onFocusChangeCallback
    }

    fun clearError() {
        setError("")
    }

    fun setError(errorMsg: String, errorIconResId: Int? = null, errorIconColorResId: Int? = null) {
        if (errorMsg.isNotEmpty()) {
            binding.tilLayout.error = errorMsg
            setErrorIcon(errorIconResId, errorIconColorResId)
        } else {
            binding.tilLayout.error = null
        }
    }

    fun setError(errorMsgId: Int, errorIconResId: Int? = null, errorIconColorResId: Int? = null) {
        setError(binding.root.context.getString(errorMsgId), errorIconResId, errorIconColorResId)
    }

    fun removeErrorIfCurrentIs(resId: Int) {
        if (isPresentError(resId)) {
            clearError()
        }
    }

    fun isPresentError(errResId: Int): Boolean = isPresentError(resources.getString(errResId))

    private fun isPresentError(errorMsg: String): Boolean = binding.tilLayout.error == errorMsg

    fun setCustomIcon(iconResId: Int?, iconColorId: Int? = null) {
        binding.tilLayout.apply {
            iconResId?.let {
                endIconDrawable = getEndIconDrawable(it)
            } ?: kotlin.run {
                endIconDrawable = null
            }
            iconColorId?.let {
                val iconColor = ContextCompat.getColor(context, it)
                setEndIconTintList(ColorStateList.valueOf(iconColor))
            } ?: run {
                setEndIconTintList(null)
            }
        }
    }

    private fun getEndIconDrawable(endIconResId: Int): Drawable {
        val endIconDrawable = ContextCompat.getDrawable(context, endIconResId)
        // 這裡做if判斷是為了 與先前手動使用layer-list xml 的 layout 相容
        return if (endIconDrawable is LayerDrawable) {
            endIconDrawable
        } else {
            LayerDrawable(arrayOf(endIconDrawable)).apply {
                setLayerInsetStart(0, UiUtils.dpToPx(END_ICON_LEFT_OFFSET))
                setLayerInsetEnd(0, UiUtils.dpToPx(END_ICON_LEFT_OFFSET))
            }
        }
    }

    private fun setErrorIcon(iconResId: Int?, iconColorId: Int? = null) {
        binding.tilLayout.apply {
            iconResId?.let {
                errorIconDrawable = ContextCompat.getDrawable(context, it)
            } ?: kotlin.run {
                errorIconDrawable = null
            }
            iconColorId?.let {
                val iconColor = ContextCompat.getColor(context, it)
                setErrorIconTintList(ColorStateList.valueOf(iconColor))
            } ?: run {
                setErrorIconTintList(null)
            }
        }
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        textWatcherList.add(textWatcher)
        binding.tietEditText.addTextChangedListener(textWatcher)
    }

    // 這是為了讓既有的code不要修改而新增
    fun addTextChanged(textChangedFinished: (Editable) -> Unit) {
        val watcher = object : BaseTextWatcher() {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChangedFinished(editable: Editable) {
                textChangedFinished(editable)
            }
        }
        addTextChangedListener(watcher)
    }

    fun clearTextChangedListener() {
        textWatcherList.forEach { textWatcher ->
            binding.tietEditText.removeTextChangedListener(textWatcher)
        }
        textWatcherList.clear()
    }

    override fun setEnabled(enabled: Boolean) {
        with(binding.tilLayout) {
            val bgColorResId: Int = if (enabled) {
                R.color.transparent
            } else {
                R.color.snow
            }
            boxBackgroundColor = ContextCompat.getColor(context, bgColorResId)

            endIconDrawable?.let {
                val endIconColorStateList: ColorStateList? = if (enabled) {
                    null
                } else {
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.iron))
                }
                setEndIconTintList(endIconColorStateList)
            }
            isEnabled = enabled
        }
        binding.tietEditText.isEnabled = enabled
        binding.tilLayout.isEnabled = enabled
        super.setEnabled(enabled)
    }

    fun checkTextAndShowError(
        pattern: Pattern = CHINESE_PATTERN,
        checkEmpty: Boolean = true,
        emptyErrorResId: Int = R.string.common_edittext_must_not_empty_error,
        checkFormat: Boolean = true,
        formatErrorResId: Int = R.string.common_input_format_error,
    ): Boolean {
        val error = when {
            checkEmpty && text.isBlank() -> Pair(true, resources.getString(emptyErrorResId))
            checkFormat && text.isNotBlank() && !pattern.matcher(text).matches() -> Pair(true, resources.getString(formatErrorResId))
            else -> Pair(false, "")
        }
        setError(error.second)
        return error.first
    }

    fun setInputType(inputType: Int) {
        binding.tietEditText.inputType = inputType
    }

    // otp state convenient function
    fun setVerifySuccessState(iconResId: Int = R.drawable.ic_circle_success_filled, iconColorId: Int = R.color.apple) {
        isEnabled = false
        binding.tilLayout.apply {
            error = ""
            setCustomIcon(iconResId, iconColorId)
        }
    }

    fun setVerifyFailState(errMsg: String, iconResId: Int = R.drawable.ic_circle_error_filled, iconColorId: Int = R.color.red_orange) {
        isEnabled = true
        binding.tilLayout.apply {
            error = errMsg
            setErrorIcon(iconResId, iconColorId)
        }
    }

    private fun toDropDownListMode() {
        binding.tietEditText.run {
            isClickable = true
            isFocusable = false
        }
    }

    fun addInputFilters(vararg inputFilters: InputFilter) {
        filters = inputFilterSet.run {
            addAll(inputFilters)
            toTypedArray()
        }
    }

    fun removeInputFilter(inputFilter: InputFilter) {
        filters = inputFilterSet.run {
            remove(inputFilter)
            toTypedArray()
        }
    }

    private fun getCurrentHintText(hasFocus: Boolean): String = if (floatingTitle.isBlank()) {
        hint
    } else if (hasFocus) {
        floatingTitle
    } else if (text.isBlank()) {
        hint
    } else {
        floatingTitle
    }

    fun isError(): Boolean = binding.tilLayout.error?.isNotBlank() ?: false

    fun setErrMsgSingleLine(isErrMsgSingleLine: Boolean) {
//        binding.tilLayout.findViewById<TextView>(R.id.textinput_error).isSingleLine = isErrMsgSingleLine
    }

    override fun clearFocus() {
        binding.tietEditText.clearFocus()
    }

    fun requestEditTextFocus() {
        binding.tietEditText.requestFocus()
    }

    fun showKeyboardWithDelay() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(KEY_BOARD_DELAY_MILLIS)
            requestEditTextFocus()
            binding.tietEditText.let {
                UiUtils.showKeyboard(it)
                it.setSelection(it.length())
            }
        }
    }

    fun getInputType(): Int = binding.tietEditText.inputType

    fun setTypeface(typeface: Typeface?) {
        binding.tietEditText.typeface = typeface
    }

    fun getTypeface(): Typeface? = binding.tietEditText.typeface

    fun setEndIconOnClickListener(endIconOnClickCallback: EndIconOnClickCallback?) {
        this.endIconOnClickCallback = endIconOnClickCallback
        binding.tilLayout.setEndIconOnClickListener { //init後會空出一塊點擊範圍給endIcon
            endIconOnClickCallback?.onClick(this@TextInputLayout, it)
        }
    }

    fun putCursorToTextTail() {
        binding.tietEditText.setSelection(binding.tietEditText.length())
    }

    override fun isFocused(): Boolean = binding.tietEditText.isFocused

    fun reset() {
        text = ""
        clearError()
        helperText = ""
    }

    interface EndIconOnClickCallback {
        fun onClick(til: TextInputLayout, view: View)
    }

    companion object {
        const val DEFAULT_TIET_INPUT_TYPE = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        const val INPUT_TYPE_TEXT_PASSWORD_MASK =
            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        const val INPUT_TYPE_TEXT_PASSWORD = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        val CHINESE_PATTERN: Pattern = Pattern.compile("^[0-9a-zA-Z_\u4e00-\u9fa5]+$")
        private const val DEFAULT_RES_ID = 0
        private const val DEFAULT_COLOR = -1
        private const val DEFAULT_TIET_MAX_LENGTH = 50
        private const val END_ICON_LEFT_OFFSET = 16
        private val KEY_BOARD_DELAY_MILLIS = TimeUnit.MILLISECONDS.toMillis(300)
    }
}
