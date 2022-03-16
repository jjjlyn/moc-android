package androidx.recyclerview.widget

import android.graphics.Rect
import android.view.View

class ItemGapDecoration : RecyclerView.ItemDecoration {
    private val gap: Rect
    @RecyclerView.Orientation
    private val orientation: Int

    constructor(gap: Rect, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL) {
        this.gap = gap
        this.orientation = orientation
    }

    constructor(gap: Int, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL):
            this(gap, gap, gap, gap, orientation)

    constructor(left: Int = 0,
                top: Int = 0,
                right: Int = 0,
                bottom: Int = 0,
                @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL):
            this(Rect(left, top, right, bottom), orientation)

    constructor(horizontal: Int = 0,
                vertical: Int = 0,
                @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL):
            this(horizontal, vertical, horizontal, vertical, orientation)

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val count = parent.adapter?.itemCount ?: 0
        if (count <= 0) return

        val pos = parent.getChildAdapterPosition(view)
        if (pos == RecyclerView.NO_POSITION) return

        outRect.run {
            left += gap.left
            right += gap.right
            top += gap.top
            bottom += gap.bottom
        }
//        when (pos) {
//            0 -> outRect.run {
//                if (orientation == RecyclerView.VERTICAL) left += gap.left
//                else top += gap.top
//                right += gap.right
//                bottom += gap.bottom
//            }
//            count - 1 ->outRect.run {
//                if (orientation == RecyclerView.VERTICAL) right += gap.right
//                else bottom += gap.bottom
//                left += gap.left
//                top += gap.top
//            }
//            else -> outRect.run {
//                left += gap.left
//                right += gap.right
//                top += gap.top
//                bottom += gap.bottom
//            }
//        }
    }
}