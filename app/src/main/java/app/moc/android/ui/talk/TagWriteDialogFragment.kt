package app.moc.android.ui.talk

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import app.moc.android.R
import app.moc.android.databinding.TagWriteDialogFragmentBinding
import app.moc.android.ui.common.CommonAlertDialogFragment
import app.moc.android.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TagWriteDialogFragment(
    private val tagAction: TagAction,
) : CommonAlertDialogFragment(R.layout.tag_write_dialog_fragment) {

    private lateinit var binding: TagWriteDialogFragmentBinding
    private val talkWriteViewModel: TalkWriteViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TagWriteDialogFragmentBinding.bind(view)
        with(binding) {
            editTextTag.apply {
                clearFocus()
                requestFocus()
            }
            if (tagAction is TagAction.Modify) {
                val oldTag = tagAction.oldTag
                editTextTag.apply {
                    setText(oldTag)
                    setSelection(oldTag.length)
                }
            }
            setOnLeftClick {
                dismiss()
            }
            setOnRightClick {
                val currentTag = editTextTag.editableText.toString()
                if (currentTag.isEmpty()) {
                    showToast("태그를 입력해주세요")
                    return@setOnRightClick
                }
                when (tagAction) {
                    is TagAction.Add -> {
                        talkWriteViewModel.addTag(currentTag)
                    }
                    is TagAction.Modify -> {
                        val position = tagAction.position
                        talkWriteViewModel.modifyTag(position, currentTag)
                    }
                }
                dismiss()
            }
        }
    }
}