package app.moc.android.util.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.moc.android.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MocPermissionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PERMISSIONS = "permissions"
        const val EXTRA_RATIONALE_TITLE = "rationale_title"
        const val EXTRA_RATIONALE_MESSAGE = "rationale_message"
        const val EXTRA_DENY_TITLE = "deny_title"
        const val EXTRA_DENY_MESSAGE = "deny_message"
        var permissionListenerStack: Deque<OnPermissionListener>? = null

        fun startActivity(context: Context, intent: Intent, listener: OnPermissionListener){
            if(permissionListenerStack == null){
                permissionListenerStack = ArrayDeque()
            }
            permissionListenerStack!!.push(listener)
            context.startActivity(intent)
        }
    }

    private var rationaleTitle: CharSequence? = null
    private var rationaleMessage: CharSequence? = null
    private var isShownRationaleDialog: Boolean = false
    private var denyTitle: CharSequence? = null
    private var denyMessage: CharSequence? = null
    private var settingButtonText: String? = null

    private var permissions: Array<String>? = null

    private var requestPermissionResult: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.forEach { permission ->
                Timber.d("${permission.key} ${permission.value}")
            }

            val deniedPermissions: List<String> =
                MocPermissionBase.getDeniedPermissions(this, *permissions.keys.toTypedArray())

            if (deniedPermissions.isEmpty()) {
                permissionResult(null)
            } else {
                showPermissionDenyDialog(deniedPermissions)
            }
        }

    private val requestSettingsResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                checkPermissions(true)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        setupFromSavedInstanceState(savedInstanceState)

        checkPermissions(false)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        // TODO status temporarily save
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun showPermissionDenyDialog(deniedPermissions: List<String>) {
        if(denyMessage.isNullOrEmpty()){
            permissionResult(deniedPermissions)
            return
        }

        val builder = AlertDialog.Builder(this, R.style.Widget_Moc_Dialog_Alert)
            .setMessage(denyMessage)
            .setCancelable(false)
            .setNegativeButton("아니오"){ _, _ ->
                permissionResult(deniedPermissions)
            }

        if(settingButtonText.isNullOrEmpty()){
            settingButtonText = "네(설정 보기)"
        }

        builder.setPositiveButton(settingButtonText){ _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:$packageName"))
            requestSettingsResult.launch(intent)
        }

        builder.show()
    }

    internal fun checkPermissions(fromOnActivityResult: Boolean) {
        val needPermissions = arrayListOf<String>()

        permissions?.forEach { permission ->
            if (MocPermissionBase.isDenied(this, permission)) {
                needPermissions += permission
            }
        }

        if(needPermissions.isEmpty()){
            permissionResult(null)
        } else if(fromOnActivityResult){
            permissionResult(needPermissions)
        } else {
            requestPermissionResult.launch(needPermissions.toTypedArray())
        }
    }

    private fun permissionResult(deniedPermissions: List<String>?) {
        Timber.v(MocPermission.TAG, "permissionResult() : $deniedPermissions")

        finish()

        if(permissionListenerStack != null){
            val listener = permissionListenerStack!!.pop()

            if(deniedPermissions.isNullOrEmpty()){
                listener.onPermissionGranted()
            } else {
                listener.onPermissionDenied(deniedPermissions)
            }

            if(permissionListenerStack!!.isEmpty()){
                permissionListenerStack = null
            }
        }
    }

    private fun showRationalDialog(needPermissions: List<String>) {
        AlertDialog.Builder(this)
            .setMessage(rationaleMessage)
            .setCancelable(false)
            .setPositiveButton("네(설정 보기)") { _, _ ->
                requestPermissionResult.launch(needPermissions.toTypedArray())
            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.cancel()
            }
            .show()

        isShownRationaleDialog = true
    }

    private fun setupFromSavedInstanceState(savedInstanceState: Bundle?){
        // if configuration is changed (화면 회전, 언어 설정 변경 등)
        if(savedInstanceState != null){
            permissions = savedInstanceState.getStringArray(EXTRA_PERMISSIONS)
            rationaleTitle = savedInstanceState.getCharSequence(EXTRA_RATIONALE_TITLE)
            rationaleMessage = savedInstanceState.getCharSequence(EXTRA_RATIONALE_MESSAGE)
            denyTitle = savedInstanceState.getCharSequence(EXTRA_DENY_TITLE)
            denyMessage = savedInstanceState.getCharSequence(EXTRA_DENY_MESSAGE)
        } else {
            // 새로 화면 진입 시
            val intent = intent

            permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS)
            rationaleTitle = intent.getCharSequenceExtra(EXTRA_RATIONALE_TITLE)
            rationaleMessage = intent.getCharSequenceExtra(EXTRA_RATIONALE_MESSAGE)
            denyTitle = intent.getCharSequenceExtra(EXTRA_DENY_TITLE)
            denyMessage = intent.getCharSequenceExtra(EXTRA_DENY_MESSAGE)
        }
    }
}