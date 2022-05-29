package app.moc.android.ui.career.history.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinePagerIndicatorDecoration
import androidx.recyclerview.widget.PagerSnapHelper
import app.moc.android.R
import app.moc.android.databinding.CalendarItemDialogFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.common.CommonAlertDialogFragment
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CalendarItemDialogFragment(
    private val careerItemUIModel: CareerItemUIModel,
    private val calendarItemUIModel: CalendarItemUIModel
): CommonAlertDialogFragment(R.layout.calendar_item_dialog_fragment) {

    private lateinit var binding: CalendarItemDialogFragmentBinding
    private val viewModel: CalendarItemDialogViewModel by viewModels()
    private lateinit var imageAdapter: CalendarItemDialogImageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageAdapter = CalendarItemDialogImageAdapter()
        if(calendarItemUIModel.imageTag != null){
            viewModel.getImages(calendarItemUIModel.imageTag)
        }
        binding = CalendarItemDialogFragmentBinding.bind(view)
        with(binding){
            isImageEmpty = calendarItemUIModel?.imageTag == null
            careerItemUIModel = this@CalendarItemDialogFragment.careerItemUIModel
            calendarItemUIModel = this@CalendarItemDialogFragment.calendarItemUIModel
            listImage.apply {
                PagerSnapHelper().attachToRecyclerView(this)
                addItemDecoration(LinePagerIndicatorDecoration(requireActivity()))
                adapter = imageAdapter
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.images.collectLatest { result ->
                if(result is Result.Success){
                    val data = result.data
                    binding.isImageEmpty = data.isEmpty()
                    imageAdapter.submitList(data)
                }
            }
        }
    }
}