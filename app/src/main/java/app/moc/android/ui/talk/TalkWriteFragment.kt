package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemMarginDecoration
import app.moc.android.R
import app.moc.android.databinding.TalkWriteFragmentBinding
import app.moc.android.ui.common.CancelAlertDialogFragment
import app.moc.android.ui.common.CommonTwoButtonDialogFragment
import app.moc.android.util.isDialogShowing
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TalkWriteFragment : Fragment(R.layout.talk_write_fragment), TalkWriteActionHandler {

    private lateinit var binding: TalkWriteFragmentBinding
    private lateinit var talkTagAdapter: TalkTagAdapter
    private val talkWriteViewModel: TalkWriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        talkTagAdapter = TalkTagAdapter().apply {
            onTagDeleted = { tag ->
                val oldList = talkTagAdapter.currentList.toMutableList()
                val newList = oldList.apply {
                    remove(tag)
                }
                talkTagAdapter.submitList(newList.toList())
            }
            onTagModified = { position, tag ->
                if(childFragmentManager.isDialogShowing().not()){
                    TagWriteDialogFragment(TagAction.Modify(position, tag.substring(2)))
                        .show(childFragmentManager, TagWriteDialogFragment::class.java.simpleName)
                }
            }
        }
        binding = TalkWriteFragmentBinding.bind(view)
        with(binding){
            header.apply {
                actionHandler = this@TalkWriteFragment
                header.textTitle.text = "글쓰기"
                header.buttonConfirm.text = "완료"
                header.buttonCancel.text = "취소"
            }
            containerCategory
                .apply {
                    textCategory.hint = "카테고리를 선택해주세요"
                }
                .root
                .setOnClickListener {
                    if(childFragmentManager.isDialogShowing()) {
                        return@setOnClickListener
                    }
                    TalkCategoryBottomSheetDialogFragment()
                        .show(childFragmentManager, TalkCategoryBottomSheetDialogFragment::class.java.simpleName)
                }
            containerTitle.textTitle.hint = "제목을 입력해주세요"
            containerContent.textContent.hint = "내용을 입력해주세요 (최대 2000자)"
            imageCamera.setOnClickListener {

            }
            textTag.setOnClickListener {
                // 태그 입력창으로 이동
                if(childFragmentManager.isDialogShowing()) {
                    return@setOnClickListener
                }
                TagWriteDialogFragment(TagAction.Add)
                    .show(childFragmentManager, TagWriteDialogFragment::class.java.simpleName)
            }
            listTag.apply {
                addItemDecoration(
                    ItemMarginDecoration(horizontal = resources.getDimensionPixelOffset(R.dimen.spacing_xmicro))
                )
                adapter = talkTagAdapter
            }

            lifecycleScope.launchWhenStarted {
                launch {
                    talkWriteViewModel.talkCategory.collectLatest {
                        binding.containerCategory.textCategory.text = it.label
                    }
                }

                launch {
                    talkWriteViewModel.onTagAdded.collectLatest { tag ->
                        val oldList = talkTagAdapter.currentList.toMutableList()
                        val newList = oldList.apply {
                            add("# $tag")
                        }
                        talkTagAdapter.submitList(newList.toList())
                    }
                }

                launch {
                    talkWriteViewModel.onTagModified.collectLatest { (position, tag) ->
                        val oldList = talkTagAdapter.currentList.toMutableList()
                        val newList = oldList.apply {
                            removeAt(position)
                            add(position, "# $tag")
                        }
                        talkTagAdapter.submitList(newList.toList())
                    }
                }
            }
        }
    }

    override fun showCancelDialog() {
        if(childFragmentManager.isDialogShowing()) return
        CancelAlertDialogFragment()
            .show(childFragmentManager, CancelAlertDialogFragment::class.java.simpleName)
    }

    override fun onConfirmClick() {

    }
}