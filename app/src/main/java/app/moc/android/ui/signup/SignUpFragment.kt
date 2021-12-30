package app.moc.android.ui.signup

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.SignUpFragmentBinding
import app.moc.android.util.getDrawableCompat
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment: MainNavigationFragment(R.layout.sign_up_fragment) {

    private lateinit var binding: SignUpFragmentBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_background)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignUpFragmentBinding.bind(view).apply {
            viewModel = signUpViewModel
            setOnEndIconClick {
                signUpViewModel.checkEmailDuplicate()
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                signUpViewModel.isNextEnabled.collectLatest { isEnabled ->
                    binding.isNextEnabled = isEnabled
                }
            }

            launch {
                signUpViewModel.navigationAction.collectLatest { action ->
                    when(action){
                        SignUpNavigationAction.NavigateToDetail -> {
                            findNavController().navigate(SignUpFragmentDirections.toSignUpDetail(
                                email = signUpViewModel.email.value,
                                pwd = signUpViewModel.pwd.value
                            ))
                        }
                    }
                }
            }

            launch {
                signUpViewModel.email.collectLatest {
                    setEmailValid(false)
                }
            }

            launch {
                signUpViewModel.emailUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    val isValid = result.isLoading.not() && result is Result.Success
                    setEmailValid(isValid)
                }
            }
        }
    }

    override fun onDestroy() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.status_bar)
        super.onDestroy()
    }

    private fun setEmailValid(isValid: Boolean) {
        val previousState = binding.isEmailValid
        if (previousState != isValid) {
            binding.isEmailValid = isValid
            binding.textInputEmail.endIconDrawable =
                requireActivity().getDrawableCompat(if (isValid) R.drawable.ic_text_valid else R.drawable.ic_text_duplicate_check)
            binding.textInputEmail.isEndIconChanged = true
        }
    }
}
