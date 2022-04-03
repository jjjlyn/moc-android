package app.moc.android.util.permission

class MocPermissionResult(private val deniedPermissions: List<String>?) {

    private var granted: Boolean = false

    init {
        this.granted = deniedPermissions.isNullOrEmpty()
    }

    fun isGranted(): Boolean {
        return granted
    }

    fun getDeniedPermissions() : List<String>? {
        return deniedPermissions
    }
}