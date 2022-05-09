package app.moc.android.ui.talk

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.moc.android.R
import app.moc.android.databinding.TagWriteDialogFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TagWriteDialogFragment : DialogFragment(R.layout.tag_write_dialog_fragment) {

    private lateinit var binding: TagWriteDialogFragmentBinding
    private val talkWriteViewModel: TalkWriteViewModel by viewModels({ requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TagWriteDialogFragmentBinding.bind(view)
        with(binding){
            setOnLeftClick {
                dismiss()
            }
            setOnRightClick {
                talkWriteViewModel.addTag(editTextTag.editableText.toString())
                dismiss()
            }
        }
        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }

}