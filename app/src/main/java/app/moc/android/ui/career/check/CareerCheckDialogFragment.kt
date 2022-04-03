package app.moc.android.ui.career.check

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import app.moc.android.R
import app.moc.android.databinding.CareerCheckDialogFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.util.imagepicker.MocImagePicker
import app.moc.android.util.imagepicker.MocImagePickerActivity
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.model.DateTime
import app.moc.model.PlanCheck
import app.moc.shared.result.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CareerCheckDialogFragment: DialogFragment(R.layout.career_check_dialog_fragment) {

    private lateinit var binding: CareerCheckDialogFragmentBinding
    private val careerCheckDialogViewModel: CareerCheckDialogViewModel by viewModels()
    private lateinit var careerItemUIModel: CareerItemUIModel
    private lateinit var careerCheckAdapter: CareerCheckAdapter

    private var requestActivity: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val intent = activityResult.data
                if(intent != null){
                    val selectedUris = MocImagePickerActivity.getSelectedUris(intent)
                    showMultiImage(selectedUris ?: emptyList())
                }
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            careerItemUIModel = it.get("careerItemUIModel") as CareerItemUIModel
        }
        careerCheckAdapter = CareerCheckAdapter()
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
            containerCamera.root.setOnClickListener {
                showImagePicker()
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

    private fun showImagePicker() {
        MocImagePicker.with(requireActivity())
            .errorListener { e ->

            }
            .min(1, minCountMessage = "미만")
            .max(maxCount = 10, maxCountMessage = "초과")
            .selectedUris(careerCheckAdapter.selectedUris)
            .startMultiImage(
                onSelected = { list ->
                    showMultiImage(list)
                },
                onGranted = { c, builder ->
                    requestActivity.launch(MocImagePickerActivity.getIntent(c, builder))
                }
            )
    }

    private fun showMultiImage(uris: List<Uri>) {
        // caching previous selected views (다시 image picker로 전환 시 이전 selected views state를 저장해야 한다.)
        uris.forEach {
            Timber.d("uri : $it")
        }
        careerCheckAdapter.submitList(uris)
        updateSelectedUris(uris)
    }

    private fun updateSelectedUris(uris: List<Uri>) {
        careerCheckAdapter.selectedUris = uris.toMutableList()
        updateSelectedUriExist()
    }

    private fun updateSelectedUriExist(){
//        binding.isSelectedUriExist = careerCheckAdapter.selectedUris.isNotEmpty()
    }
}