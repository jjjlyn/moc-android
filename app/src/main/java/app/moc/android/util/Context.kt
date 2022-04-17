package app.moc.android.util

import android.content.Context
import android.text.SpannableString
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes colorResId: Int) =
    ContextCompat. getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
    ContextCompat.getColorStateList(this, colorResId)

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int) =
    ContextCompat.getDrawable(this, drawableResId)

fun Context.showKeyboard(view: View){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(view: View){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}