package androidx.recyclerview.widget

import android.graphics.Rect
import android.view.View

class ItemMarginDecoration : RecyclerView.ItemDecoration {

    private val margin: Rect
    private var orientation: Int = RecyclerView.DEFAULT_ORIENTATION

    var inset: Int = 0

    constructor(margin: Rect, @RecyclerView.Orientation orientation: Int) {
        this.margin = margin
        this.orientation = orientation
    }

    constructor(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0, @RecyclerView.Orientation orientation: Int):
            this(Rect(left, top, right, bottom), orientation)

    constructor(margin: Int, @RecyclerView.Orientation orientation: Int):
            this(margin, margin, margin, margin, orientation)

    constructor(horizontal: Int = 0, vertical: Int = 0, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL):
            this(horizontal, vertical, horizontal, vertical, orientation)

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val count = parent.adapter?.itemCount ?: return

        val pos = parent.getChildAdapterPosition(view)
        if (pos == RecyclerView.NO_POSITION) return

        if(orientation == RecyclerView.VERTICAL){
            outRect.right = margin.right
            outRect.left = margin.left

            when(pos){
                0 -> {
                    outRect.top = inset
                    outRect.bottom = margin.bottom
                }
                count - 1 -> {
                    outRect.top = margin.top
                    outRect.bottom = 0
                }
                else -> {
                    outRect.top = margin.top
                    outRect.bottom = margin.bottom
                }
            }
        } else {
            outRect.top = margin.top
            outRect.bottom = margin.bottom

            when(pos){
                0 -> {
                    outRect.left = inset
                    outRect.right = margin.right
                }
                count - 1 -> {
                    outRect.left = margin.left
                    outRect.right = 0
                }
                else -> {
                    outRect.left = margin.left
                    outRect.right = margin.right
                }
            }
        }
    }
}