package app.moc.android.util.permission

interface OnPermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied(deniedPermissions: List<String>)
}

