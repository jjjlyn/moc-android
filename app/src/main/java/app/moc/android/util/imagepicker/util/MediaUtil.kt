package app.moc.android.util.imagepicker.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import app.moc.android.util.fmt
import app.moc.android.util.imagepicker.type.MediaType
import app.moc.model.DateTime
import java.io.*
import java.lang.Exception
import java.util.*

class MediaUtil {
    companion object {

        internal fun getMediaIntentUri(
            context: Context,
            mediaType: MediaType,
            savedDirectoryName: String?
        ): Pair<Intent, Uri> {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (cameraIntent.resolveActivity(context.packageManager) == null) {
                throw PackageManager.NameNotFoundException("Can not start Camera")
            }

            return getMediaUri(context, cameraIntent, mediaType, savedDirectoryName)
        }

        private fun getMediaUri(
            context: Context,
            cameraIntent: Intent,
            mediaType: MediaType,
            savedDirectoryName: String?
        ): Pair<Intent, Uri> {
            val timeStamp = "YYYY_MM_DD_HH_MM_SS".fmt(DateTime())
            val fileName = "${mediaType}_$timeStamp"
            val folderName = "MOC"
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val directoryName =
                    if (savedDirectoryName != null) {
                        "${mediaType.savedDirectoryName}/$folderName/$savedDirectoryName"
                    } else {
                        "${mediaType.savedDirectoryName}/$folderName"
                    }

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName + mediaType.fileSuffix)
                    put(MediaStore.MediaColumns.MIME_TYPE, mediaType.mimeType)
                    put(MediaStore.MediaColumns.RELATIVE_PATH, directoryName)
                }

                val mediaUri =
                    context.contentResolver.insert(mediaType.externalContentUri, contentValues)!!
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri)
                cameraIntent to mediaUri
            } else {
                val directoryName = if(savedDirectoryName != null) "${folderName}/${savedDirectoryName}" else "${mediaType.savedDirectoryName}/${folderName}"
                val directory = Environment.getExternalStoragePublicDirectory(directoryName)
                if (directory.exists().not()) {
                    directory.mkdir()
                }

                val file = File.createTempFile(fileName, mediaType.fileSuffix, directory)

                val mediaUri = FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName + ".providers",
                    file
                )

                val resolvedIntentActivities = context.packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)

                resolvedIntentActivities.forEach { resolvedIntentInfo ->
                    val packageName = resolvedIntentInfo.activityInfo.packageName
                    context.grantUriPermission(
                        packageName,
                        mediaUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri)
                cameraIntent to Uri.fromFile(file)
            }
        }

        fun scanMedia(context: Context, uri: Uri, onSuccess: () -> Unit) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(uri.path),
                null
            ) { _, _ ->
                onSuccess()
            }
        }

        fun deleteMedia(context: Context, uri: Uri) {
            context.contentResolver.delete(uri, null, null)
        }

        fun processResult(context: Context, uri: Uri) {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                var bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, null, true)
                bitmapSaveToFileCached(context, bitmap)
                bitmap.recycle()
                inputStream?.close()
            }
        }

        private fun bitmapSaveToFileCached(context: Context, bitmap: Bitmap){
            val file = File.createTempFile(System.currentTimeMillis().toString(), ".jpg", context.cacheDir)
            try {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch(e: FileNotFoundException){
                e.printStackTrace()
            } catch (e: IOException){
                e.printStackTrace()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        private fun getRealPathFromUri(context: Context, uris: List<Uri>): List<String> {
            val list = arrayListOf<String>()
            uris.forEach { uri ->
                val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)

                if(cursor != null && cursor.count > 0){
                    cursor.moveToNext()
                    val path = cursor.getString(cursor.getColumnIndex(GalleryUtil.INDEX_MEDIA_URI))
                    cursor.close()
                    list.add(path)
                }
            }
            return list
        }

        fun uriToFile(context: Context, uris: List<Uri>): List<File> {
            val list: MutableList<File> = arrayListOf()
            if (checkFileSize(uris)) {
                resizeImage(uris)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                getRealPathFromUri(context, uris).forEach { filePath ->
                    val file = File(filePath)
                    file.mkdirs()
                    list.add(file)
                }
            } else {
                uris.map { uri ->
                    val file = File(uri.path)
                    file.mkdirs()
                    list.add(file)
                }
            }

            return list
        }

        private fun resizeImage(uris: List<Uri>) {
            uris.forEach { uri ->
                val out: OutputStream
                out = FileOutputStream(File(uri.path))

                val bitmap = BitmapFactory.decodeFile(uri.path)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }
        }

        private fun checkFileSize(uris: List<Uri>): Boolean {
            var sum: Long = 0
            uris.forEach { uri ->
                val file = File(uri.path)
                sum += file.length()
            }
            return sum >= 31457280
        }
    }
}