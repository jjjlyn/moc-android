package app.moc.android.util.imagepicker.listener

import android.net.Uri

interface OnMultiSelectedListener {
    fun onSelected(uris: List<Uri>)
}