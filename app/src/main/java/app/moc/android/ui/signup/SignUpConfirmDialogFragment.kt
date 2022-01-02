package app.moc.android.ui.signup

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.moc.android.R
import app.moc.android.databinding.SignUpConfirmDialogFragmentBinding
import app.moc.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpConfirmDialogFragment: DialogFragment(R.layout.sign_up_confirm_dialog_fragment) {

    private lateinit var binding: SignUpConfirmDialogFragmentBinding
    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.get("user") as? User ?: return
        binding = SignUpConfirmDialogFragmentBinding.bind(view).apply {
            setOnClick {
                signUpViewModel.onBoardingComplete(user)
                dismiss()
            }
        }
        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }
}