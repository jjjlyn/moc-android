package app.moc.android.ui.signin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.SignInFragmentBinding
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.model.SignIn
import app.moc.shared.result.Result
import app.moc.shared.result.data
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SignInFragment : MainNavigationFragment(R.layout.sign_in_fragment) {

    private val signInViewModel: SignInViewModel by viewModels()

    private lateinit var binding: SignInFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.color_background)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SignInFragmentBinding.bind(view).apply {
            viewModel = signInViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        launchAndRepeatWithViewLifecycle {
            combine(signInViewModel.email, signInViewModel.pwd) { email, pwd ->
                SignIn(email, pwd)
            }.collectLatest {
                binding.signIn = it
            }
        }

        launchAndRepeatWithViewLifecycle {
            signInViewModel.signInUseCaseResult.collectLatest { result ->
                // user 저장 (data store)

            }
        }
    }

    override fun onDestroy() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.status_bar)
        super.onDestroy()
    }
}