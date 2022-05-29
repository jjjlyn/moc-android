package app.moc.android.ui.common

import android.graphics.drawable.Drawable
import androidx.annotation.Keep

@Keep
data class ComponentTitleUIModel(
    val image: Drawable? = null,
    val text: String,
    val button: String? = null,
    val isListEmpty: Boolean = false
)