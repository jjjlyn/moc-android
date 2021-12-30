package app.moc.android.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import app.moc.android.R
import app.moc.android.util.dp
import app.moc.android.util.setMargins
import app.moc.android.util.setVisible
import com.google.android.material.textfield.TextInputLayout

class DrawableTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), TextWatcher {

    private var clearTextIcon : ImageView? = null
    private lateinit var frameLayout: FrameLayout

    private val focusChangeListener: OnFocusChangeListener = OnFocusChangeListener { _: View, hasFocus: Boolean ->
    val editText = editText ?: return@OnFocusChangeListener
        if(hasFocus){
            clearTextIcon?.setVisible(editText.text.isNotEmpty())
        } else {
            clearTextIcon?.setVisible(false)
        }
    }

    var isEndIconChanged : Boolean = false
        set(value) {
            if(value){
                initClearTextIcon(endIconMode != END_ICON_NONE)
            }
            field = false
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextInputLayout, defStyleAttr, 0)
        isEndIconChanged = a.getBoolean(R.styleable.DrawableTextInputLayout_isEndIconChanged, this.isEndIconChanged)
        a.recycle()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (child is FrameLayout) {
            frameLayout = child
        }

        if(child is EditText) {
            editText ?: return
            init()
        }
    }

    private fun init() {
        val editText = editText ?: return
        initClearTextIcon(endIconMode != END_ICON_NONE)
        editText.onFocusChangeListener = focusChangeListener
        editText.addTextChangedListener(this)
    }

    private fun initClearTextIcon(hasEndIconMode: Boolean) {
        clearTextIcon?.let {
            frameLayout.removeView(it)
        }
        val editText = editText ?: return
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_text_clear) ?: return

        val clearTextIcon = LayoutInflater.from(context).inflate(R.layout.text_clear_item, frameLayout, false) as ImageView
        with(clearTextIcon) {
            maxHeight = editText.height
            setImageDrawable(drawable)
            setOnClickListener {
                editText.setText("")
            }
        }

        if (hasEndIconMode) {
            val shiftedClearTextIcon = clearTextIcon
            val endIconDrawable = endIconDrawable ?: return
            val endIconWidth = endIconDrawable.intrinsicWidth
            shiftedClearTextIcon.setMargins(0, 0,
                    if(endIconWidth <= 30.dp().toInt()) endIconWidth * 2 else endIconWidth, 0
            )
            frameLayout.addView(shiftedClearTextIcon)
        } else {
            frameLayout.addView(clearTextIcon)
        }
        clearTextIcon.setVisible(false)

        this.clearTextIcon = clearTextIcon
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val editText = editText ?: return
        if(editText.isFocused){
            clearTextIcon?.setVisible(s.isNotEmpty())
        }
    }
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}
}