package app.moc.android.util.permission

import android.content.Context

class MocPermission: MocPermissionBase() {
    companion object {
        val TAG = MocPermission::class.java.simpleName

        fun with(context: Context) : Builder {
            return Builder(context)
        }

        class Builder(context: Context) : PermissionBuilder<Builder>(context) {
            fun check(
                loading: () -> Unit,
                onSuccess: (MocPermissionResult) -> Unit,
                onError: (Throwable) -> Unit
            ) {
                loading()
                val permissionListener : OnPermissionListener = object: OnPermissionListener {
                    override fun onPermissionGranted() {
                        onSuccess(MocPermissionResult(null))
                    }

                    override fun onPermissionDenied(deniedPermissions: List<String>) {
                        onSuccess(MocPermissionResult(deniedPermissions))
                    }
                }
                try {
                    setPermissionListener(permissionListener)
                    checkPermissions()
                } catch(e: Throwable){
                    onError(e)
                }
            }
        }
    }
}