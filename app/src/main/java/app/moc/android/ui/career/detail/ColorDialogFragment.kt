package app.moc.android.ui.career.detail

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemGapDecoration
import app.moc.android.R
import app.moc.android.databinding.ColorDialogFragmentBinding
import app.moc.android.ui.career.ColorActionHandler
import app.moc.android.ui.career.colors
import app.moc.android.ui.common.CommonAlertDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ColorDialogFragment: CommonAlertDialogFragment(R.layout.color_dialog_fragment), ColorActionHandler {
    private lateinit var binding: ColorDialogFragmentBinding
    private lateinit var colorAdapter: ColorAdapter
    private val careerDetailViewModel: CareerDetailViewModel by viewModels({requireParentFragment()})

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorAdapter = ColorAdapter()
        binding = ColorDialogFragmentBinding.bind(view).apply {
            actionHandler = this@ColorDialogFragment
            with(listColor){
                setItemViewCacheSize(24)
                addItemDecoration(
                    ItemGapDecoration(
                        horizontal = resources.getDimensionPixelOffset(R.dimen.spacing_normal),
                        vertical = resources.getDimensionPixelOffset(R.dimen.spacing_micro)
                    )
                )
                adapter = colorAdapter
            }
        }
        colorAdapter.submitList(colors)
        colorAdapter.currentSelection = careerDetailViewModel.color.value
    }

    override fun chooseColor() {
        careerDetailViewModel.onColorChanged(colorAdapter.currentSelection)
        dismiss()
    }
}