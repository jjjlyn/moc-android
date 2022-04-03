package app.moc.android.util.imagepicker.listener

import app.moc.model.Media


internal interface OnItemClickListener {

    /**
     * @param itemPosition 헤더(카메라 아이콘)를 제외한 상대적 position
     * @param layoutPosition 리스트 내 실제 position
     */
    fun onItemClick(data: Media, itemPosition: Int, layoutPosition: Int)

    fun onHeaderClick()
}