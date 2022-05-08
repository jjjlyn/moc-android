package app.moc.android.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams
import app.moc.android.R
import app.moc.android.util.dp
import app.moc.android.util.getColorCompat

class CareerProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.progressBarStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    @Px var progressBarWidth = 200.dp().toInt()
        set(value){
            field = value
            updateProgressBar()
        }

    @Px var progressBarHeight = 4.dp().toInt()
        set(value){
            field = value
            updateProgressBar()
        }

    @Px
    var radius = context.resources.getDimension(R.dimen.radius_2xsmall)
        set(value){
            field = value
            updateProgressBar()
        }

    @ColorInt
    var progressBackgroundColor = context.getColorCompat(R.color.color_background_300)
        set(value){
            field = value
            updateProgress()
        }

    @ColorInt var progressForegroundColor = context.getColorCompat(R.color.color_primary)
        set(value){
            field = value
            updateProgress()
        }

    var progress = 0.0f
        set(value){
            field = value
            updateProgressBar()
        }

    private val progressView = View(context)

    init {
        context.withStyledAttributes(attrs, R.styleable.CareerProgressBar, defStyleAttr, 0) {
            progressBarWidth = getDimensionPixelSize(R.styleable.CareerProgressBar_android_layout_width, progressBarWidth)
            progressBarHeight = getDimensionPixelSize(R.styleable.CareerProgressBar_android_layout_height, progressBarHeight)
            progress = getFloat(R.styleable.CareerProgressBar_progress, progress)
            progressBackgroundColor = getColor(R.styleable.CareerProgressBar_progressBackgroundColor, progressBackgroundColor)
            progressForegroundColor = getColor(R.styleable.CareerProgressBar_progressForegroundColor, progressForegroundColor)
            radius = getDimension(R.styleable.CareerProgressBar_android_radius, radius)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateProgressBar()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        progressBarWidth = w
    }

    private fun updateFixedSize() {
        post {
            updateLayoutParams {
                width = progressBarWidth
                height = progressBarHeight
            }
        }
    }

    private fun updateContainer() {
        background = GradientDrawable().apply {
            cornerRadius = radius
            setColor(progressBackgroundColor)
        }
    }

    private fun updateProgress() {
        with(progressView){
            post {
                updateLayoutParams {
                    width = (progressBarWidth * progress).toInt()
                    height = progressBarHeight
                }
                background = GradientDrawable().apply {
                    cornerRadius = radius
                    setColor(progressForegroundColor)
                }
            }
        }
    }

    private fun updateProgressBar(){
        updateFixedSize()
        updateContainer()
        updateProgress()
        updateChildViews()
    }

    private fun updateChildViews(){
        removeAllViews()
        addView(progressView)
        invalidate()
    }
}