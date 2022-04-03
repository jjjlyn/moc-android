package app.moc.android.ui.mypage

import android.os.Bundle
import android.view.View
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.SettingsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : MainNavigationFragment(R.layout.settings_fragment){
    private lateinit var binding: SettingsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SettingsFragmentBinding.bind(view)
        with(binding){
            
        }
    }
}