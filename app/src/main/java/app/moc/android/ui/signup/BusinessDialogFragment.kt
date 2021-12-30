package app.moc.android.ui.signup

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import app.moc.android.R
import app.moc.android.databinding.BusinessDialogFragmentBinding
import app.moc.model.Business
import app.moc.model.businessMain
import app.moc.model.businessSub
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessDialogFragment: DialogFragment(R.layout.business_dialog_fragment) {

    private lateinit var binding: BusinessDialogFragmentBinding
    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })
    private lateinit var businessMainAdapter: BusinessAdapter
    private lateinit var businessSubAdapter: BusinessAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BusinessDialogFragmentBinding.bind(view).apply {
            setOnClick {
                businessSubAdapter.currentSelection?.let {
                    selectBusiness(it)
                }
            }
        }
        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
        setupUI()
        loadData()
    }

    private fun loadData() {
        businessMainAdapter.submitList(businessMain)
        businessSubAdapter.submitList(signUpViewModel.getBusinessByMain(businessMain[0]))
    }

    private fun setupUI() {
        businessMainAdapter = BusinessAdapter().apply {
            currentSelection = businessMain[0]
            onMainSelectionChanged = {
                with(businessSubAdapter){
                    val filteredList = signUpViewModel.getBusinessByMain(it)
                    submitList(filteredList)
                    currentSelection = filteredList[0]
                }
            }
        }
        businessSubAdapter = BusinessAdapter().apply {
            currentSelection = businessSub[0]
        }
        binding.listMain.adapter = businessMainAdapter
        binding.listSub.adapter = businessSubAdapter
    }

    private fun selectBusiness(business: Business){
        signUpViewModel.onBusinessChanged(business)
        dismiss()
    }
}