package app.moc.android.ui.signup

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.moc.model.DateTime
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LeaveDatePickerDialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(picker: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        selectLeaveDate(c.time)
    }

    private fun selectLeaveDate(date: DateTime){
        signUpViewModel.onLeaveDateChanged(date)
    }
}