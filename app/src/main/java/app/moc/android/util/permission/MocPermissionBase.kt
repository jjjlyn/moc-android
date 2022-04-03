package app.moc.android.util.permission

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class MocPermissionBase {
    companion object {
        const val PREFS_NAME_PERMISSION = "PREFS_NAME_PERMISSION"
        const val PREFS_IS_FIRST_REQUEST = "IS_FIRST_REQUEST"

        fun isGranted(context: Context, vararg permissions: String) : Boolean {
            return permissions.all { permission ->
                isDenied(context, permission).not()
            }
        }

        fun isDenied(context: Context, permission: String) : Boolean {
            return isGranted(context, permission).not()
        }

        private fun isGranted(context: Context, permission: String) : Boolean {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        fun getDeniedPermissions(context: Context, vararg permissions: String) : List<String> {
            val deniedPermissions = arrayListOf<String>()
            permissions.forEach { permission ->
                if(isDenied(context, permission)){
                    deniedPermissions += permission
                }
            }
            return deniedPermissions
        }

        fun canRequestPermission(activity: Activity, vararg permissions: String) : Boolean {
            if(isFirstRequest(activity, permissions.map { it }.toTypedArray())){
                return true
            }

            return permissions.all { permission ->
                val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                isDenied(activity, permission).not() && showRationale
            }
        }

        private fun isFirstRequest(context: Context, permissions: Array<String>) : Boolean {
            return permissions.all { permission ->
                isFirstRequest(context, permission)
            }
        }

        private fun isFirstRequest(context: Context, permission: String) : Boolean {
            return getSharedPreferences(context).getBoolean(getPrefsNamePermission(permission), true)
        }

        private fun getPrefsNamePermission(permission: String) : String {
            return PREFS_IS_FIRST_REQUEST + "_" + permission
        }

        private fun getSharedPreferences(context: Context) : SharedPreferences {
            return context.getSharedPreferences(PREFS_NAME_PERMISSION, Context.MODE_PRIVATE)
        }

        internal fun  setFirstRequest(context: Context, permissions: Array<String>){
            permissions.forEach { permission ->
                setFirstRequest(context, permission)
            }
        }

        private fun setFirstRequest(context: Context, permission: String){
            getSharedPreferences(context).edit().putBoolean(getPrefsNamePermission(permission), true).apply()
        }
    }
}