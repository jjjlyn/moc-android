package app.moc.android.ui.talk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import app.moc.android.databinding.TalkCategoryBottomSheetDialogFragmentBinding
import app.moc.android.ui.talk.TalkCategory.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TalkCategoryBottomSheetDialogFragment : BottomSheetDialogFragment(){
    private lateinit var binding: TalkCategoryBottomSheetDialogFragmentBinding
    private lateinit var talkCategoryAdapter: TalkCategoryAdapter
    private val talkWriteViewModel: TalkWriteViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TalkCategoryBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        talkCategoryAdapter = TalkCategoryAdapter(viewLifecycleOwner).apply {
            onCategoryChanged = { category ->
                talkWriteViewModel.setTalkCategory(category)
                dismiss()
            }
            talkCategory = talkWriteViewModel.talkCategory
        }
        with(binding){
            listCategory.apply {
                adapter = talkCategoryAdapter
            }
        }
        talkCategoryAdapter.submitList(
            listOf(DAILY, QUESTION, WORRIES, RESIGNATION_REVIEW)
        )
    }
}