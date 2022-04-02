package app.moc.android.ui.career.check

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import app.moc.android.R
import app.moc.android.databinding.CareerCheckDialogFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.model.DateTime
import app.moc.model.PlanCheck
import app.moc.shared.result.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CareerCheckDialogFragment: DialogFragment(R.layout.career_check_dialog_fragment) {

    private lateinit var binding: CareerCheckDialogFragmentBinding
    private val careerCheckDialogViewModel: CareerCheckDialogViewModel by viewModels()
    private lateinit var careerItemUIModel: CareerItemUIModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            careerItemUIModel = it.get("careerItemUIModel") as CareerItemUIModel
        }
        binding = CareerCheckDialogFragmentBinding.bind(view).apply {
            setOnLeftClick {
                dismiss()
            }
            setOnRightClick {
                careerCheckDialogViewModel.registerCareerCheck(PlanCheck(
                    id = careerItemUIModel.id,
                    date = DateTime().time,
                    type = careerItemUIModel.type,
                    satisfact = careerCheckDialogViewModel.satisfact.value,
                    imageTag = null
                ))
            }
            chipGroup.setOnCheckedChangeListener { group, checkedId ->
                val idx = group.checkedChipIds.indexOf(checkedId)
                careerCheckDialogViewModel.setSatisfact(idx + 1)
            }
        }
        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }

        lifecycleScope.launch {
            careerCheckDialogViewModel.registerCheckResult
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if (result is Result.Success) {
                        // TODO: 토스트 메시지
                        dismiss()
                    }
                }
        }
    }
}