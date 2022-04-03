package app.moc.android.util.permission

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes

abstract class PermissionBuilder<T : PermissionBuilder<T>>(private val context: Context) {

    private var listener: OnPermissionListener? = null
    private var permissions: Array<String>? = null
    private var rationaleTitle: CharSequence? = null
    private var rationaleMessage: CharSequence? = null
    private var denyTitle: CharSequence? = null
    private var denyMessage: CharSequence? = null
    private var settingsButtonText: CharSequence? = null

    protected fun checkPermissions(){
        if(listener == null){
            throw IllegalArgumentException("You must set setPermissionListener() on Permission")
        } else if(permissions.isNullOrEmpty()){
            throw IllegalArgumentException("You must set setPermissions() on Permission")
        }

        val intent = Intent(context, MocPermissionActivity::class.java).apply {
            putExtra(MocPermissionActivity.EXTRA_PERMISSIONS, permissions)
            putExtra(MocPermissionActivity.EXTRA_RATIONALE_TITLE, rationaleTitle)
            putExtra(MocPermissionActivity.EXTRA_RATIONALE_MESSAGE, rationaleMessage)
            putExtra(MocPermissionActivity.EXTRA_DENY_TITLE, denyTitle)
            putExtra(MocPermissionActivity.EXTRA_DENY_MESSAGE, denyMessage)

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
        }

        MocPermissionActivity.startActivity(context, intent, listener!!)
        MocPermissionBase.setFirstRequest(context, permissions!!)
    }

    fun setPermissionListener(listener: OnPermissionListener) : T {
        this.listener = listener
        return this as T
    }

    fun setPermissions(vararg permissions: String) : T {
        this.permissions = permissions.map { it }.toTypedArray()
        return this as T
    }

    @SuppressLint("ResourceType")
    fun getText(@StringRes stringRes: Int) : CharSequence {
        if(stringRes <= 0){
            throw IllegalArgumentException("Invalid String resource id")
        }
        return context.getText(stringRes)
    }

    fun setRationaleMessage(@StringRes stringRes: Int) : T {
        return setRationaleMessage(getText(stringRes))
    }

    fun setRationaleMessage(rationaleMessage: CharSequence) : T {
        this.rationaleMessage = rationaleMessage
        return this as T
    }

    fun setRationaleTitle(@StringRes stringRes: Int) : T {
        return setRationaleTitle(stringRes)
    }

    fun setRationaleTitle(rationaleTitle: CharSequence) : T {
        this.rationaleTitle = rationaleTitle
        return this as T
    }

    fun setDeniedTitle(@StringRes stringRes: Int) : T {
        return setDeniedTitle(stringRes)
    }

    fun setDeniedTitle(deniedTitle: CharSequence) : T {
        this.denyTitle = deniedTitle
        return this as T
    }

    fun setDeniedMessage(@StringRes stringRes: Int) : T {
        return setDeniedMessage(stringRes)
    }

    fun setDeniedMessage(deniedMessage: CharSequence) : T {
        this.denyMessage = deniedMessage
        return this as T
    }

    fun setNavigateToSettingsButtonText(@StringRes stringRes: Int): T {
        return setNavigateToSettingsButtonText(getText(stringRes))
    }

    fun setNavigateToSettingsButtonText(settingsButtonText: CharSequence): T {
        this.settingsButtonText = settingsButtonText
        return this as T
    }
}