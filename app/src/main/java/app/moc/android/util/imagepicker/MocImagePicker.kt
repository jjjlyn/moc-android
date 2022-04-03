package app.moc.android.util.imagepicker

import android.content.Context
import android.net.Uri
import app.moc.android.util.imagepicker.listener.OnErrorListener
import app.moc.android.util.imagepicker.listener.OnMultiSelectedListener
import app.moc.android.util.imagepicker.listener.OnNavigateToImagePickerListener
import java.lang.ref.WeakReference

class MocImagePicker {
    companion object {
        @JvmStatic
        fun with(context: Context) = Builder(WeakReference(context))
    }

    class Builder(private val contextWeakReference: WeakReference<Context>) :
        MocImagePickerBaseBuilder<Builder>() {

        fun errorListener(onErrorListener: OnErrorListener): Builder {
            this.onErrorListener = onErrorListener
            return this
        }

        fun errorListener(action: (Throwable?) -> Unit): Builder {
            this.onErrorListener = object : OnErrorListener {
                override fun onError(throwable: Throwable?) {
                    action.invoke(throwable)
                }
            }
            return this
        }

        fun navigationListener(action: () -> Unit): Builder {
            this.onNavigateToImagePicker = object : OnNavigateToImagePickerListener {
                override fun navigate() {
                    action.invoke()
                }
            }
            return this
        }

        fun navigationListener(onNavigateToImagePicker: OnNavigateToImagePickerListener): Builder {
            this.onNavigateToImagePicker = onNavigateToImagePicker
            return this
        }

        fun startMultiImage(
            onSelected: (List<Uri>) -> Unit,
            onGranted: (Context, MocImagePickerBaseBuilder<*>) -> Unit
        ) {
            onMultiSelectedListener = object : OnMultiSelectedListener {
                override fun onSelected(uris: List<Uri>) {
                    onSelected(uris)
                }
            }

            start(onGranted)
        }

        private fun start(onGranted: (Context, MocImagePickerBaseBuilder<*>) -> Unit) {
            contextWeakReference.get()?.let { context ->
                checkPermission(context,
                loading = {

                },
                onSuccess = { permissionResult ->
                    val isGranted = permissionResult.isGranted()
                    if (isGranted) {
                        onGranted(context, this)
                    } else {
                        onErrorListener?.onError()
                    }
                },
                onError = { e ->
                    onErrorListener?.onError(e)
                })
            }
        }
    }
}