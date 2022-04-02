package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemMarginDecoration
import app.moc.android.R
import app.moc.android.databinding.TalkMainFragmentBinding
import app.moc.android.ui.home.MocTalkAdapter
import app.moc.android.ui.home.toUIModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TalkMainFragment : Fragment(R.layout.talk_main_fragment) {

    private lateinit var binding: TalkMainFragmentBinding
    private lateinit var talkMainAdapter: ConcatAdapter
    private lateinit var filterAdapter: TalkFilterAdapter
    private lateinit var mocTalkAdapter: MocTalkAdapter
    private val talkMainViewModel: TalkMainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterAdapter = TalkFilterAdapter()
        mocTalkAdapter = MocTalkAdapter()
        talkMainAdapter = ConcatAdapter(filterAdapter, mocTalkAdapter)
        binding = TalkMainFragmentBinding.bind(view)
        with(binding){
            listTalk.apply {
//                addItemDecoration(ItemMarginDecoration(vertical = resources.getDimensionPixelOffset(R.dimen.stroke_small)))
                adapter = talkMainAdapter
            }
            fab.setOnClickListener {
                findNavController().navigate(TalkMainFragmentDirections.toTalkWrite())
            }
        }
        filterAdapter.submitList(listOf(TalkFilterUIModel(-1, "latest")))
        lifecycleScope.launch {
            talkMainViewModel.latestCommunities
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                mocTalkAdapter.submitData(it.map { item -> item.toUIModel() })
            }
        }
    }
}