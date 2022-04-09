package app.moc.android.ui.talk

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

data class TalkFilterUIModel(
    val category: Int,
    val filterType: String // latest, popular
)

data class TalkDetailFooterItemUIModel(
    val text: String,
    val image: Drawable?
)