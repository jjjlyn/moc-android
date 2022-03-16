package app.moc.android.ui.signup

import androidx.fragment.app.viewModels
import app.moc.android.ui.common.CommonDatePickerDialogFragment
import app.moc.model.DateTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaveDatePickerDialogFragment: CommonDatePickerDialogFragment() {

    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    fun selectLeaveDate(date: DateTime){
        signUpViewModel.onLeaveDateChanged(date)
    }
}