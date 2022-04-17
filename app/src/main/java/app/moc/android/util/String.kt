package app.moc.android.util

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorRes
import app.moc.android.R

fun String.toSpannableString(context: Context,
                             @ColorRes color: Int = R.color.color_on_surface,
                             typeFace: Int = Typeface.NORMAL,
                             start: Int,
                             end: Int) : SpannableString {
    return SpannableString(this).apply {
        setSpan(
            ForegroundColorSpan(context.getColorCompat(color)),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            StyleSpan(typeFace),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}