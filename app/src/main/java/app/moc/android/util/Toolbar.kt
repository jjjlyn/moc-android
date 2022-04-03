package app.moc.android.util

import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import java.lang.reflect.Field


private fun Toolbar.getActionBarTextView(): TextView? {
    var titleTextView: TextView? = null
    try {
        val f: Field = this.javaClass.getDeclaredField("mTitleTextView")
        f.isAccessible = true
        titleTextView = f.get(this) as? TextView
    } catch (e: NoSuchFieldException) {
    } catch (e: IllegalAccessException) {
    }
    return titleTextView
}

fun Toolbar.setTitleAlignCenter() {
    getActionBarTextView()?.apply {
        gravity = Gravity.CENTER_HORIZONTAL
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }
}