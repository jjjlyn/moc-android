package app.moc.android.util.imagepicker.model

import android.net.Uri
import app.moc.model.Media

internal fun Media.toUIModel() = MediaItemUIModel(albumName, uri, dateAddedSecond)

internal sealed class MediaUIModel

internal data class MediaItemUIModel(
    val albumName: String,
    val uri: Uri,
    val dateAddedSecond: Long
) : MediaUIModel()

internal fun MediaItemUIModel.toModel() = Media(albumName, uri, dateAddedSecond)


internal data class MediaHeaderUIModel(
    val title: String
) : MediaUIModel()
