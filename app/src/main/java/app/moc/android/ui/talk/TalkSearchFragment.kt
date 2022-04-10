package app.moc.android.ui.talk

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemMarginDecoration
import app.moc.android.R
import app.moc.android.databinding.TalkSearchFragmentBinding
import app.moc.android.ui.home.MocTalkItemUIModel
import app.moc.android.ui.home.toUIModel
import app.moc.android.ui.signup.keyWords
import app.moc.android.util.dp
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TalkSearchFragment : Fragment(R.layout.talk_search_fragment), TalkActionHandler {

    private lateinit var binding: TalkSearchFragmentBinding
    private lateinit var filterAdapter: TalkFilterAdapter
    private lateinit var talkSearchAdapter: TalkSearchAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private val talkSearchViewModel: TalkSearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterAdapter = TalkFilterAdapter()
        talkSearchAdapter = TalkSearchAdapter().apply {
            actionHandler = this@TalkSearchFragment
        }
        concatAdapter = ConcatAdapter(filterAdapter, talkSearchAdapter)
        binding = TalkSearchFragmentBinding.bind(view)
        with(binding){
            header.apply {
                header.textTitle.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                        talkSearchViewModel.onKeywordChanged(
                            if(p0.toString() == "") emptyList() else p0.split(" ")
                        )
                    }
                    override fun afterTextChanged(p0: Editable) {}
                })
                header.buttonOption.also { it.text = "취소" }.setOnClickListener {
                    header.textTitle.setText("")
                }
            }
            listResult.apply {
                addItemDecoration(ItemMarginDecoration(vertical = resources.getDimensionPixelOffset(R.dimen.stroke_small)))
                adapter = concatAdapter
            }
        }
        filterAdapter.submitList(listOf(TalkFilterUIModel(-1, "latest")))

        launchAndRepeatWithViewLifecycle {
            launch {
                talkSearchViewModel.keyword.collectLatest { keyword ->


                }
            }

            launch {
                talkSearchViewModel.searchResults.collectLatest { result ->
                    if(result is Result.Success){
                        val list = result.data
                        talkSearchAdapter.submitList(list.map { it.toUIModel() })
                    }
                }
            }
        }
    }

    override fun navigateToDetail(uiModel: MocTalkItemUIModel) {
        findNavController().navigate(TalkSearchFragmentDirections.toTalkDetail(uiModel))
    }
}