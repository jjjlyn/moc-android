package app.moc.android.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.SignUpDetailFragmentBinding
import app.moc.android.util.*
import app.moc.model.businessMain
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SignUpDetailFragment: MainNavigationFragment(R.layout.sign_up_detail_fragment), SignUpActionHandler {

    private lateinit var binding: SignUpDetailFragmentBinding
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val args: SignUpDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(signUpViewModel){
            onEmailChanged(args.email)
            onPwdChanged(args.pwd)
        }

        binding = SignUpDetailFragmentBinding.bind(view).apply {
            actionHandler = this@SignUpDetailFragment
            viewModel = signUpViewModel
            editNickName.setOnFocusChangeListener { v, isFocused ->
                if(isFocused){
                    v.onSelect()
                }
            }
            setOnEndIconClick { signUpViewModel.checkNickNameDuplicate() }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                signUpViewModel.isSignUpEnabled.collectLatest { isEnabled ->
                    binding.isSignUpEnabled = isEnabled
                }
            }

            launch {
                signUpViewModel.nickName.collectLatest {
                    setNickNameValid(false)
                }
            }

            launch {
                signUpViewModel.business.collectLatest { business ->
                    if(business != null){
                        val main = businessMain.find { it.categoryM == business.categoryM }?.content
                        binding.business = "$main - ${business.content}"
                    }
                }
            }

            launch {
                signUpViewModel.keyWords.collectLatest { keyWords ->
                    binding.keyWords = keyWords.joinToString { it.content }
                }
            }

            launch {
                signUpViewModel.leaveDate.collectLatest { leaveDate ->
                    if(leaveDate != null){
                        binding.leaveDate = try {
                            "yyyy/MM/dd".fmt(leaveDate)
                        } catch (e: Exception) {
                            return@collectLatest
                        }
                    }
                }
            }

            launch {
                signUpViewModel.signUpUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    when(result){
                        is Result.Success -> {
                            SignUpConfirmDialogFragment().apply {
                                isCancelable = false
                                arguments = bundleOf("user" to result.data)
                            }.show(childFragmentManager, SignUpConfirmDialogFragment::class.java.simpleName)
                        }
                        is Result.Error -> {
                            Toast.makeText(requireActivity(), "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }

            launch {
                signUpViewModel.nickNameUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    val isValid = result.isLoading.not() && result is Result.Success && result.data.result
                    setNickNameValid(isValid)
                }
            }
        }
    }

    private fun setNickNameValid(isValid: Boolean){
        val previousState = binding.isNickNameValid
        if(previousState != isValid){
            signUpViewModel.isNickNameValid.value = isValid
            binding.isNickNameValid = isValid
            binding.textInputNickName.endIconDrawable =
                requireActivity().getDrawableCompat(
                    if (isValid) R.drawable.ic_text_valid
                    else R.drawable.ic_text_duplicate_check
                )
            binding.textInputNickName.endIconPaddingEnd =
                if (isValid) requireActivity().resources.getDimensionPixelOffset(R.dimen.margin_small) else 0.dp().toInt()
            binding.textInputNickName.isEndIconChanged = true
        }
    }

    override fun showBusinessList() {
        if(childFragmentManager.isDialogShowing()) return
        binding.textInputBusiness.onSelect()
        BusinessDialogFragment()
            .apply { isCancelable = false }
            .show(childFragmentManager, BusinessDialogFragment::class.java.simpleName)
    }

    override fun showKeyWordList() {
        if(childFragmentManager.isDialogShowing()) return
        binding.textInputKeyWords.onSelect()
        KeywordsBottomSheetFragment()
            .apply { isCancelable = false }
            .show(childFragmentManager, KeywordsBottomSheetFragment::class.java.simpleName)
    }

    override fun showLeaveDatePicker() {
        if(childFragmentManager.isDialogShowing()) return
        binding.textInputLeaveDate.onSelect()
        LeaveDatePickerDialogFragment()
            .apply {
                isCancelable = false
                onDateChanged = { c, _, _, _, _ ->
                    this.selectLeaveDate(c.time)
                }
            }
            .show(childFragmentManager, LeaveDatePickerDialogFragment::class.java.simpleName)
    }

    private fun View.onSelect(){
        binding.root.allViews
            .filterIsInstance<TextView>()
            .filterNot { it == this }
            .forEach {
                it.clearFocus()
                it.isSelected = false
            }
    }
}