package app.moc.android.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.moc.android.R
import app.moc.android.databinding.OnBoardingFragmentBinding
import app.moc.android.ui.signin.SignInFragment
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.on_boarding_fragment) {

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()
    private lateinit var binding: OnBoardingFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OnBoardingFragmentBinding.bind(view).apply {
            viewModel = onBoardingViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                onBoardingViewModel.navigationAction.collectLatest { action ->
                    when(action){
                        OnBoardingNavigationAction.NavigateToSignUp -> {
                            findNavController().navigate(OnBoardingFragmentDirections.toSignUp())
                        }
                        OnBoardingNavigationAction.NavigateToSignIn -> {
                            findNavController().navigate(OnBoardingFragmentDirections.toSignIn())
                        }
                    }
                }
            }
        }
    }
}