package app.moc.android.ui.signup

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.moc.android.R
import app.moc.android.databinding.CommonOneButtonDialogFragmentBinding
import app.moc.android.ui.common.CommonAlertDialogFragment
import app.moc.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpConfirmDialogFragment: CommonAlertDialogFragment(R.layout.common_one_button_dialog_fragment) {

    private lateinit var binding: CommonOneButtonDialogFragmentBinding
    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.get("user") as? User ?: return
        binding = CommonOneButtonDialogFragmentBinding.bind(view).apply {
            setOnClick {
                signUpViewModel.onBoardingComplete(user)
                dismiss()
            }
            message = "회원가입이 완료되었습니다."
        }
    }
}