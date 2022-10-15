package com.github.lucascalheiros.feature_home.presentation.editFileMetadata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.lucascalheiros.feature_home.databinding.DialogEditFileMetadataBinding
import com.github.lucascalheiros.feature_home.presentation.editFileMetadata.model.EditFileMetadataDialogInfo
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFileMetadataDialogFragment : DialogFragment() {

    private lateinit var binding: DialogEditFileMetadataBinding
    private val editFileMetadataViewModel: EditFileMetadataDialogViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogEditFileMetadataBinding.inflate(inflater, container, true)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            dismiss()
        }

        binding.handler = editFileMetadataViewModel

        val info = arguments?.getParcelable<EditFileMetadataDialogInfo>(DIALOG_INFO_KEY)

        if (info != null) {
            editFileMetadataViewModel.setData(info)
        }

        editFileMetadataViewModel.editFileMetadataDialogState.observe(viewLifecycleOwner) {
            when (it) {
                is EditFileMetadataDialogState.Success -> {
                    dismiss()
                    editFileMetadataViewModel.handleEditFileMetadataState()
                }
                is EditFileMetadataDialogState.Cancel -> {
                    dismiss()
                    editFileMetadataViewModel.handleEditFileMetadataState()
                }
                is EditFileMetadataDialogState.Failure -> {
                    dismiss()
                    editFileMetadataViewModel.handleEditFileMetadataState()
                }
                else -> {}
            }
        }

        return binding.root
    }


    companion object {
        const val TAG = "EditFileMetadataDialog"

        const val DIALOG_INFO_KEY = "EditFileMetadataDialogInfo"

        fun newInstance(info: EditFileMetadataDialogInfo): EditFileMetadataDialogFragment {
            val fragment = EditFileMetadataDialogFragment()
            val args = Bundle()
            args.putParcelable(DIALOG_INFO_KEY, info)
            fragment.arguments = args

            return fragment
        }
    }
}