package app.moc.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.android.material.textview.MaterialTextView

@SuppressLint("RestrictedApi", "ResourceType")
class CheckableTextView constructor(
    context: Context,
    attrs: AttributeSet
): MaterialTextView(context, attrs), View.OnTouchListener {

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if(hasOnClickListeners()){
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    isSelected = true
                }
            }
        }
        return false
    }
}