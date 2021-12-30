package app.moc.android.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes colorResId: Int) =
    ContextCompat. getColor(this, colorResId)

fun Context.getColorStateListCompat(@ColorRes colorResId: Int) =
    ContextCompat.getColorStateList(this, colorResId)

fun Context.getDrawableCompat(@DrawableRes drawableResId: Int) =
    ContextCompat.getDrawable(this, drawableResId)