package app.moc.android.util.imagepicker.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import app.moc.model.Album
import app.moc.model.Media
import java.io.File

internal class GalleryUtil {

    companion object {
        private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
        const val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
        private const val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED

        private lateinit var albumName: String

        internal fun getMedia(context: Context,
                              onSuccess: (List<Album>) -> Unit,
                              onError: (Throwable) -> Unit
        ) {
            try {
                val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                albumName = MediaStore.Images.Media.BUCKET_DISPLAY_NAME

                val sortOrder = "$INDEX_DATE_ADDED DESC"
                val projection = arrayOf(INDEX_MEDIA_ID, INDEX_MEDIA_URI, albumName, INDEX_DATE_ADDED)
                val selection = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    MediaStore.Images.Media.SIZE + " > 0"
                } else {
                    null
                }
                val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)
                val listAlbum: List<Album> = cursor?.let {
                    val listTotalImage : List<Media> =
                        generateSequence { if(cursor.moveToNext()) cursor else null }
                            .map { cursor -> getImage(cursor) }
                            .filterNotNull()
                            .toList()

                    val listAlbum: List<Album> = listTotalImage.asSequence()
                        .groupBy { media -> media.albumName }
                        .toSortedMap(Comparator { albumName1, albumName2 ->
                            if(albumName2 == "Camera"){
                                1
                            } else {
                                albumName1.compareTo(albumName2, true)
                            }
                        })
                        .map { entry -> getAlbum(entry) }
                        .toList()

                    val totalAlbum : Album = listTotalImage.run {
                        val albumName = "All"
                        Album(
                            albumName,
                            getOrElse(0){ Media(albumName, Uri.EMPTY, 0) }.uri,
                            this
                        )
                    }

                    mutableListOf(totalAlbum).apply {
                        addAll(listAlbum)
                    }
                } ?: emptyList()

                cursor?.close()
                onSuccess(listAlbum)
            } catch(e: Exception){
                onError(e)
            }
        }

        private fun getAlbum(entry: Map.Entry<String, List<Media>>) =
            Album(entry.key, entry.value[0].uri, entry.value)

        private fun getImage(cursor: Cursor): Media? =
            try {
                cursor.run {
                    val folderName = getString(getColumnIndex(albumName))
                    val mediaUri = getMediaUri()
                    val dateAddedSecond = getLong(getColumnIndex(INDEX_DATE_ADDED))
                    Media(folderName, mediaUri, dateAddedSecond)
                }
            } catch(e: Exception){
                e.printStackTrace()
                null
            }

        private fun Cursor.getMediaUri(): Uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val id = getLong(getColumnIndex(INDEX_MEDIA_ID))

                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                ContentUris.withAppendedId(contentUri, id)
            } else {
                val mediaPath = getString(getColumnIndex(INDEX_MEDIA_URI))
                Uri.fromFile(File(mediaPath))
            }
    }
}