package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.moc.android.R
import app.moc.android.databinding.TalkWriteFragmentBinding
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TalkWriteFragment : Fragment(R.layout.talk_write_fragment) {

    private lateinit var binding: TalkWriteFragmentBinding
    private lateinit var talkTagAdapter: TalkTagAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        talkTagAdapter = TalkTagAdapter()
        binding = TalkWriteFragmentBinding.bind(view)
        with(binding){
            header.apply {
                header.buttonCancel.text = "취소"
                header.textTitle.text = "글쓰기"
                header.buttonConfirm.text = "완료"
            }
            containerCategory.textCategory.hint = "카테고리를 선택해주세요"
            containerTitle.textTitle.hint = "제목을 입력해주세요"
            containerContent.textContent.hint = "내용을 입력해주세요 (최대 2000자)"
            imageCamera.setOnClickListener {

            }
            textTag.setOnClickListener {
                val old = talkTagAdapter.currentList.toMutableList()
                talkTagAdapter.submitList(old.apply { add("# ") })
            }
            listTag.apply {
                layoutManager = FlexboxLayoutManager(requireActivity()).apply {

                }
                adapter = talkTagAdapter
            }
        }
    }
}