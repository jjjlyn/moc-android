package app.moc.android.ui.career.check

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.moc.android.R
import app.moc.android.databinding.CareerCheckDialogFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.common.CommonAlertDialogFragment
import app.moc.android.util.imagepicker.MocImagePicker
import app.moc.android.util.imagepicker.MocImagePickerActivity
import app.moc.android.util.imagepicker.util.MediaUtil
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.android.util.showToast
import app.moc.android.util.tryOrDefault
import app.moc.model.DateTime
import app.moc.shared.data.api.request.PlanCheckRegisterRequest
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CareerCheckDialogFragment: CommonAlertDialogFragment(R.layout.career_check_dialog_fragment) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            careerItemUIModel = it.get("careerItemUIModel") as CareerItemUIModel
        }
        careerCheckDialogViewModel.getCurrentCareerChecksUseCase(careerItemUIModel.id)
        careerCheckAdapter = CareerCheckAdapter().apply {
            onImageRemove = { uri ->
                val oldList = selectedUris.toMutableList()
                val newList = oldList.apply {
                    if(oldList.contains(uri)){
                        remove(uri)
                    }
                }
                showMultiImage(newList)
            }
        }
        binding = CareerCheckDialogFragmentBinding.bind(view).apply {
            setOnLeftClick {
                dismiss()
            }
            setOnRightClick {
                careerCheckDialogViewModel.registerCareerCheck(PlanCheckRegisterRequest(
                    planId = careerItemUIModel.id,
                    type = tryOrDefault(0) { careerItemUIModel.type.toInt() },
                    date = DateTime().time / 1000,
                    satisfact = careerCheckDialogViewModel.satisfact.value,
                    images = MediaUtil.uriToFile(requireActivity(), careerCheckAdapter.selectedUris)
                ))
            }
            chipGroup.setOnCheckedChangeListener { group, checkedId ->
                val child = group.children.find { it.id == checkedId }
                child?.let {
                    val idx = group.children.indexOf(it)
                    careerCheckDialogViewModel.setSatisfact(idx + 1)
                }
            }
            containerCamera.root.setOnClickListener {
                showImagePicker()
            }
            listPicture.adapter = careerCheckAdapter
        }

        lifecycleScope.launch {
            careerCheckDialogViewModel.registerCheckResult
                .collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if (result is Result.Success) {
                        dismiss()
                        showToast("인증되었어요 :)")
                    }
                }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                careerCheckDialogViewModel.alreadyCheckedItem.collectLatest { alreadyCheckedItem ->
                    val isAlreadyChecked = alreadyCheckedItem != null
                    binding.isAlreadyChecked = isAlreadyChecked
                    binding.textTitle.text =
                        if(isAlreadyChecked) {
                            "오늘 인증이 완료되었어요."
                        } else {
                            "오늘의 수행 만족도와\n인증사진을 남겨주세요 :)"
                        }
                    if(alreadyCheckedItem != null){
                        val chip = binding.chipGroup.getChildAt(alreadyCheckedItem.satisfact - 1)
                        binding.chipGroup.check(chip.id)
                    }
                }
            }
        }
    }

    private fun showImagePicker() {
        MocImagePicker.with(requireActivity())
            .errorListener { e ->

            }
            .min(1, minCountMessage = "미만")
            .max(maxCount = 3, maxCountMessage = "초과")
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
        binding.isConfirmEnabled = uris.isNotEmpty()
    }
}