package app.moc.android.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import app.moc.android.databinding.KeywordsBottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeywordsBottomSheetFragment: BottomSheetDialogFragment() {
    private lateinit var binding: KeywordsBottomSheetFragmentBinding
    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })
    private lateinit var keyWordsAdapter: KeyWordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = KeywordsBottomSheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyWordsAdapter = KeyWordsAdapter()
        with(binding){
            listKeyword.apply {
                adapter = keyWordsAdapter
                setHasFixedSize(true)
            }
            setOnClick {
                selectKeyWords(keyWordsAdapter.currentSelections)
            }
        }
        keyWordsAdapter.submitList(keyWords)
    }

    private fun selectKeyWords(keywords: Set<KeyWord>){
        signUpViewModel.onKeyWordsChanged(keywords)
        dismiss()
    }
}