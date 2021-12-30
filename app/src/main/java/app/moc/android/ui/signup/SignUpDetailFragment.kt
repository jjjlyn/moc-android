package app.moc.android.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
import java.text.SimpleDateFormat
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
                signUpViewModel.nickName.collectLatest { _ ->
                    setNickNameValid(false)
                }
            }

            launch {
                signUpViewModel.business.collectLatest { business ->
                    val main = businessMain.find { it.categoryM == business.categoryM }?.content
                    binding.business = "$main - ${business.content}"
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
                            SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(leaveDate)
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
                            Toast.makeText(requireActivity(), "회원가입 성공", Toast.LENGTH_SHORT).show()
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
                    val isValid = result.isLoading.not() && result is Result.Success
                    setNickNameValid(isValid)
                }
            }
        }
    }

    private fun setNickNameValid(isValid: Boolean){
        val previousState = binding.isNickNameValid
        if(previousState != isValid){
            binding.isNickNameValid = isValid
            binding.textInputNickName.endIconDrawable =
                requireActivity().getDrawableCompat(if(isValid) R.drawable.ic_text_valid else R.drawable.ic_text_duplicate_check)
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
            .apply { isCancelable = false }
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