package app.moc.android.ui.talk

import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.TalkDetailFragmentBinding
import app.moc.android.util.*
import app.moc.model.CommentModify
import app.moc.model.CommentUpload
import app.moc.shared.result.Result
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TalkDetailFragment: MainNavigationFragment(R.layout.talk_detail_fragment) {
    private lateinit var binding: TalkDetailFragmentBinding
    private val args: TalkDetailFragmentArgs by navArgs()
    private val talkDetailViewModel: TalkDetailViewModel by viewModels()
    private lateinit var talkCommentAdapter: TalkCommentAdapter
    private var tempCommentInfo : TalkCommentUIModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        talkCommentAdapter = TalkCommentAdapter().apply {
            onMoreClick = { view, uiModel ->
                if(uiModel.isMyComment){
                    view.setupMenu(requireActivity(), R.menu.talk_my_comment_item_menu){ menuItem ->
                        when(menuItem.itemId){
                            R.id.action_modify -> {
                                talkDetailViewModel.requestModifyCommentFocus(uiModel)
                            }
                            R.id.action_delete -> {
                                talkDetailViewModel.requestDeleteComment(uiModel.boardID, uiModel.commentID)
                            }
                        }
                    }
                } else {
                    view.setupMenu(
                        requireActivity(),
                        R.menu.talk_other_comment_item_menu,
                        onMenuInflate = { menu ->
                            if(uiModel.parentsID != 0){
                                menu.removeItem(R.id.action_reply)
                            }
                        }
                    ){ menuItem ->
                        when(menuItem.itemId){
                            R.id.action_reply -> {
                                binding.containerComment.textInputComment.editText?.apply {
                                    val default = "@${uiModel.nickName} ".toSpannableString(
                                            requireActivity(),
                                            typeFace = Typeface.BOLD,
                                            start = 0,
                                            end = uiModel.nickName.length + 1
                                        )
                                    setText(default)
                                    setSelection(default.length)
                                    talkDetailViewModel.requestUploadComment(uiModel)
                                }
                            }
                            R.id.action_report -> {

                            }
                        }
                    }
                }
            }
        }
        binding = TalkDetailFragmentBinding.bind(view)
        with(binding){
            uiModel = args.uiModel
            containerFooter.apply {
                with(itemLeft){
                    root.setOnClickListener {

                    }
                    uiModel = TalkDetailFooterItemUIModel(
                        args.uiModel.likes,
                        requireActivity().getDrawableCompat(R.drawable.ic_likes)
                    )
                }
                with(itemCenter){
                    root.background = null
                    uiModel = TalkDetailFooterItemUIModel(
                        "0",
                        requireActivity().getDrawableCompat(R.drawable.ic_comment)
                    )
                }
                with(itemRight){
                    root.setOnClickListener {

                    }
                    uiModel = TalkDetailFooterItemUIModel(
                        "공유",
                        requireActivity().getDrawableCompat(R.drawable.ic_share)
                    )
                }
                listComment.adapter = talkCommentAdapter
            }
            containerComment.apply {
                textInputComment.setEndIconOnClickListener {
                    // 공란 validation
                    val content = textInputComment.editText?.text.toString()
                    if(content.isEmpty()){
                        showToast("내용을 입력해주세요")
                        return@setEndIconOnClickListener
                    }
                    val info = tempCommentInfo
                    if(info == null){ // 댓글
                        talkDetailViewModel.uploadComment(
                            CommentUpload(args.uiModel.id.toLong(), content, 0)
                        )
                        return@setEndIconOnClickListener
                    }
                    if(info.isMyComment){ // 댓글 수정
                        talkDetailViewModel.modifyComment(
                            CommentModify(args.uiModel.id.toLong(), info.commentID, content)
                        )
                    } else { // 대댓글
                        talkDetailViewModel.uploadComment(
                            CommentUpload(args.uiModel.id.toLong(), content, info.commentID)
                        )
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            launch {
                talkDetailViewModel.onCommentUpload.collectLatest { uiModel ->
                    tempCommentInfo = uiModel
                    binding.containerComment.textInputComment.editText?.showCommentView()
                }
            }

            launch {
                talkDetailViewModel.onCommentUploaded.collectLatest { result ->
                    if(result is Result.Success){
                        binding.containerComment.textInputComment.editText?.dismissCommentView()
                        talkDetailViewModel.getComments()
                        tempCommentInfo = null
                    }
                }
            }

            launch {
                talkDetailViewModel.onCommentModify.collectLatest { uiModel ->
                    tempCommentInfo = uiModel
                    binding.containerComment.textInputComment.editText?.apply {
                        showCommentView()
                        setText(uiModel.content)
                        setSelection(uiModel.content.length)
                    }
                }
            }

            launch {
                talkDetailViewModel.onCommentModified.collectLatest { result ->
                    if(result is Result.Success){
                        binding.containerComment.textInputComment.editText?.dismissCommentView()
                        talkDetailViewModel.getComments()
                        tempCommentInfo = null
                    }
                }
            }

            launch {
                talkDetailViewModel.onCommentDelete.collectLatest { (boardID, commentID) ->
                    if(childFragmentManager.isDialogShowing()) return@collectLatest
                    CommentDeleteAlertDialog().apply {
                        message = "댓글을 삭제하시겠어요?"
                        leftButton = "취소"
                        rightButton = "삭제"
                        onLeftClick = {
                            dismiss()
                        }
                        onRightClick = {
                            talkDetailViewModel.deleteComment(boardID.toString(), commentID.toString())
                            dismiss()
                        }
                    }.show(childFragmentManager, CommentDeleteAlertDialog::class.java.simpleName)
                }
            }

            launch {
                talkDetailViewModel.onCommentDeleted.collectLatest { result ->
                    if(result is Result.Success) {
                        talkDetailViewModel.getComments()
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
                        talkCommentAdapter.submitList(list.mapIndexed { idx, item -> item.copy(isLastItem = list.size -1 == idx) })
                        binding.containerFooter.itemCenter.uiModel = TalkDetailFooterItemUIModel(
                            list.size.toString(),
                            requireActivity().getDrawableCompat(R.drawable.ic_comment)
                        )
                        binding.isCommentEmpty = list.isEmpty()
                    }
                }
            }
        }
    }

    private fun EditText.showCommentView(){
        clearFocus()
        requestFocus()
    }

    private fun EditText.dismissCommentView(){
        clearFocus()
        setText("")
        requireActivity().hideKeyboard(this)
    }
}