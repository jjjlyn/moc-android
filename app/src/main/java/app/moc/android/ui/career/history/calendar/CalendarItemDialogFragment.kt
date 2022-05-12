package app.moc.android.ui.career.history.calendar

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import app.moc.android.R
import app.moc.android.databinding.CalendarItemDialogFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.common.CommonAlertDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarItemDialogFragment(
    private val careerItemUIModel: CareerItemUIModel,
    private val calendarItemUIModel: CalendarItemUIModel
): CommonAlertDialogFragment(R.layout.calendar_item_dialog_fragment) {

    private lateinit var binding: CalendarItemDialogFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarItemDialogFragmentBinding.bind(view)
        with(binding){
            careerItemUIModel = this@CalendarItemDialogFragment.careerItemUIModel
            calendarItemUIModel = this@CalendarItemDialogFragment.calendarItemUIModel
        }
    }
}