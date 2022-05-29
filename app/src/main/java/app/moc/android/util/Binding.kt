package app.moc.android.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import app.moc.android.util.glide.GlideApp
import app.moc.android.util.glide.GlideRequest
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.textfield.TextInputLayout

inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit){
    block()
    executePendingBindings()
}

@BindingAdapter("app:setEndIconOnClick")
fun TextInputLayout.setEndIconOnClick(onClick: View.OnClickListener?){
    setEndIconOnClickListener(onClick)
}

@BindingAdapter("src")
fun ImageView.setSrc(url: String?) = loadAsync(url)

fun ImageView.loadAsync(url: String?, @DrawableRes placeholder: Int? = null) {
    if (url == null) {
        GlideApp.with(context)
            .load(placeholder)
            .into(this)
    } else {
        loadAsync(url, block = {
            if (placeholder != null) {
                placeholder(placeholder)
            }
            transition(withCrossFade())
        })
    }
}

private inline fun ImageView.loadAsync(url: String?, block: GlideRequest<Drawable>.() -> Unit) {
    GlideApp.with(context)
        .load(url)
        .apply(block)
        .priority(Priority.IMMEDIATE)
        .into(this)
}
