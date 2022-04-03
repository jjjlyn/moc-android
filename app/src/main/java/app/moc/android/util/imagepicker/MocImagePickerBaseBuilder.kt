package app.moc.android.util.imagepicker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.annotation.Keep
import app.moc.android.util.imagepicker.listener.OnErrorListener
import app.moc.android.util.imagepicker.listener.OnMultiSelectedListener
import app.moc.android.util.imagepicker.listener.OnNavigateToImagePickerListener
import app.moc.android.util.imagepicker.type.MediaType
import app.moc.android.util.permission.MocPermission
import app.moc.android.util.permission.MocPermissionResult
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
open class MocImagePickerBaseBuilder<B : MocImagePickerBaseBuilder<B>>(
    internal val mediaType: MediaType = MediaType.IMAGE,
    internal var title: String? = null,
    internal var savedDirectoryName: String? = null,
    internal var selectedUris: List<Uri>? = null,
    internal var maxCount: Int = Int.MAX_VALUE,
    internal var maxCountMessage: String? = null,
    internal var minCount: Int = Int.MIN_VALUE,
    internal var minCountMessage: String? = null,
    internal var imageCountFormat: String = "%s"
) : Parcelable {
    @IgnoredOnParcel
    protected var onMultiSelectedListener : OnMultiSelectedListener? = null

    @IgnoredOnParcel
    protected var onErrorListener : OnErrorListener? = null

    @IgnoredOnParcel
    protected var onNavigateToImagePicker : OnNavigateToImagePickerListener? = null

    internal fun checkPermission(
        context: Context,
        loading: () -> Unit,
        onSuccess: (MocPermissionResult) -> Unit,
        onError: (Throwable) -> Unit
    ) = MocPermission.with(context)
        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .setDeniedMessage("사진 첨부 기능을 위한 [설정] > [권한]에서 ‘저장'권한을 허용해주세요")
        .setNavigateToSettingsButtonText("Settings")
        .check(loading, onSuccess, onError)

    private fun onComplete(data: Intent) {
        val selectedUris = MocImagePickerActivity.getSelectedUris(data)
        if(selectedUris != null){
            onMultiSelectedListener?.onSelected(selectedUris)
        } else {
            onErrorListener?.onError(IllegalStateException("selectedUris can not be null."))
        }
    }

    fun savedDirectoryName(savedDirectoryName: String): B {
        this.savedDirectoryName = savedDirectoryName
        return this as B
    }

    fun max(maxCount: Int, maxCountMessage: String): B {
        this.maxCount = maxCount
        this.maxCountMessage = maxCountMessage
        return this as B
    }

    fun min(minCount: Int, minCountMessage: String): B {
        this.minCount = minCount
        this.minCountMessage = minCountMessage
        return this as B
    }

    fun selectedUris(uris: List<Uri>?): B {
        this.selectedUris = uris
        return this as B
    }
}