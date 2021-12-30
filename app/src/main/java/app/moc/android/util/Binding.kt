package app.moc.android.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import com.google.android.material.textfield.TextInputLayout

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit){
    block()
    executePendingBindings()
}

@BindingAdapter("app:setEndIconOnClick")
fun TextInputLayout.setEndIconOnClick(onClick: View.OnClickListener?){
    setEndIconOnClickListener(onClick)
}
