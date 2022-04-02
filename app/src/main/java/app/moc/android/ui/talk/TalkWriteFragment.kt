package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.moc.android.R
import app.moc.android.databinding.TalkWriteFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TalkWriteFragment : Fragment(R.layout.talk_write_fragment) {

    private lateinit var binding: TalkWriteFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TalkWriteFragmentBinding.bind(view)
        with(binding){

        }
    }
}