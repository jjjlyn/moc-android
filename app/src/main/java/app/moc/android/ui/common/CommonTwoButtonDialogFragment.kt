package app.moc.android.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import app.moc.android.R
import app.moc.android.databinding.CommonTwoButtonDialogFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class CommonTwoButtonDialogFragment: DialogFragment(R.layout.common_two_button_dialog_fragment) {
    internal lateinit var binding: CommonTwoButtonDialogFragmentBinding
    var message: String? = null
    var leftButton: String? = "취소"
    var rightButton: String? = "확인"
    var onLeftClick: (() -> Unit)? = null
    var onRightClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CommonTwoButtonDialogFragmentBinding.bind(view).apply {
            message = this@CommonTwoButtonDialogFragment.message
            leftButton = this@CommonTwoButtonDialogFragment.leftButton
            rightButton = this@CommonTwoButtonDialogFragment.rightButton
            setOnLeftClick { this@CommonTwoButtonDialogFragment.onLeftClick?.invoke() }
            setOnRightClick { this@CommonTwoButtonDialogFragment.onRightClick?.invoke() }
        }
        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }
}