package app.moc.android.util

import android.app.Activity
import android.content.res.Resources
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu

// view.layoutParams.width = 10.dp() same in xml android:layout_width="10dp"
fun Float.dp() = (this * Resources.getSystem().displayMetrics.density)
fun Double.dp() = (this * Resources.getSystem().displayMetrics.density)
fun Int.dp() = (this * Resources.getSystem().displayMetrics.density)
fun Long.dp() = (this * Resources.getSystem().displayMetrics.density)

fun View.setVisible(isVisible: Boolean){
    visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun View.setMargins(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p: ViewGroup.MarginLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        if(left != null){
            p.leftMargin = left
        }
        if(top != null){
            p.topMargin = top
        }
        if(right != null){
            p.rightMargin = right
        }
        if(bottom != null){
            p.bottomMargin = bottom
        }
        this.requestLayout()
    }
}

fun View.setupMenu(
    activity: Activity,
    @MenuRes menu: Int,
    onMenuItemClickListener: (MenuItem) -> Unit
) {
    val popUpMenu = PopupMenu(activity, this)
    activity.menuInflater.inflate(menu, popUpMenu.menu)
    popUpMenu.setOnMenuItemClickListener {
        onMenuItemClickListener(it)
        false
    }
    popUpMenu.show()
}