package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import app.moc.android.R
import app.moc.android.databinding.TalkSearchFragmentBinding
import app.moc.android.ui.home.MocTalkAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TalkSearchFragment : Fragment(R.layout.talk_search_fragment){

    private lateinit var binding: TalkSearchFragmentBinding
    private lateinit var filterAdapter: TalkFilterAdapter
    private lateinit var mocTalkAdapter: MocTalkAdapter
    private lateinit var talkSearchAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterAdapter = TalkFilterAdapter()
        mocTalkAdapter = MocTalkAdapter()
        talkSearchAdapter = ConcatAdapter(filterAdapter, mocTalkAdapter)
        binding = TalkSearchFragmentBinding.bind(view)
        with(binding){
            header.apply {
                header.buttonOption.also { it.text = "취소" }.setOnClickListener {

                }
            }
            listResult.adapter = talkSearchAdapter
        }
        filterAdapter.submitList(listOf(TalkFilterUIModel(-1, "latest")))
    }
}