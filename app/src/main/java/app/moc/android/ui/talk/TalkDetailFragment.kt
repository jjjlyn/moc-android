package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.TalkDetailFragmentBinding
import app.moc.android.util.getDrawableCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TalkDetailFragment: MainNavigationFragment(R.layout.talk_detail_fragment) {
    private lateinit var binding: TalkDetailFragmentBinding
    private val args: TalkDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TalkDetailFragmentBinding.bind(view)
        with(binding){
            uiModel = args.uiModel
            containerFooter.apply {
                itemLeft.uiModel = TalkDetailFooterItemUIModel(
                    args.uiModel.likes,
                    requireActivity().getDrawableCompat(R.drawable.ic_likes)
                )
                itemCenter.uiModel = TalkDetailFooterItemUIModel(
                    "2",
                    requireActivity().getDrawableCompat(R.drawable.ic_comment)
                )
                itemRight.uiModel = TalkDetailFooterItemUIModel(
                    "공유",
                    requireActivity().getDrawableCompat(R.drawable.ic_share)
                )
            }
        }
    }
}