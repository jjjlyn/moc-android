package app.moc.android.util

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.EditText

fun Activity.registerKeyboardHandler(){
    window.decorView.viewTreeObserver.addOnGlobalFocusChangeListener { oldView, newView ->
        newView?.let {
            if(it is EditText) {
                Handler(Looper.getMainLooper()).postDelayed({
                    showKeyboard(it)
                }, 200L)
            } else {
                hideKeyboard(it)
            }
        }
    }
}