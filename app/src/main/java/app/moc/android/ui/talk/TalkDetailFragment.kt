package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.TalkDetailFragmentBinding
import app.moc.android.util.getDrawableCompat
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.model.CommentUpload
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TalkDetailFragment: MainNavigationFragment(R.layout.talk_detail_fragment) {
    private lateinit var binding: TalkDetailFragmentBinding
    private val args: TalkDetailFragmentArgs by navArgs()
    private val talkDetailViewModel: TalkDetailViewModel by viewModels()
    private lateinit var talkCommentAdapter: TalkCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        talkDetailViewModel.getComments(args.uiModel.id.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        talkCommentAdapter = TalkCommentAdapter()
        binding = TalkDetailFragmentBinding.bind(view)
        with(binding){
            uiModel = args.uiModel
            containerFooter.apply {
                itemLeft.uiModel = TalkDetailFooterItemUIModel(
                    args.uiModel.likes,
                    requireActivity().getDrawableCompat(R.drawable.ic_likes)
                )
                binding.containerFooter.itemCenter.uiModel = TalkDetailFooterItemUIModel(
                    "0",
                    requireActivity().getDrawableCompat(R.drawable.ic_comment)
                )
                itemRight.uiModel = TalkDetailFooterItemUIModel(
                    "공유",
                    requireActivity().getDrawableCompat(R.drawable.ic_share)
                )
                listComment.adapter = talkCommentAdapter
            }
            containerComment.apply {
                textInputComment.setEndIconOnClickListener {
                    // 공란 validation
                    talkDetailViewModel.uploadComment(CommentUpload(
                        boardID = args.uiModel.id.toLong(),
                        content = binding.containerComment.textInputComment.editText?.text.toString(),
                        parentsID = 0
                    ))
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            launch {
                talkDetailViewModel.onCommentUpload.collectLatest { result ->
                    if(result is Result.Success){
                        binding.containerComment.textInputComment.editText?.setText("")
                        talkDetailViewModel.getComments(args.uiModel.id.toString())
                    }
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                talkDetailViewModel.comments.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        val list = result.data
                        talkCommentAdapter.submitList(list.mapIndexed { idx, item -> item.toUIModel().copy(isLastItem = list.size -1 == idx) })
                        binding.containerFooter.itemCenter.uiModel = TalkDetailFooterItemUIModel(
                            list.size.toString(),
                            requireActivity().getDrawableCompat(R.drawable.ic_comment)
                        )
                    }
                }
            }
        }
    }
}