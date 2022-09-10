package com.github.lucascalheiros.booklibrarymanager.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.booklibrarymanager.databinding.FragmentHomeBinding
import com.github.lucascalheiros.booklibrarymanager.ui.dialogs.editFileMetadata.EditFileMetadataDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()

    private val filePickerResultRegister =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { homeViewModel.uploadFile(it) } ?: Log.d(
                    TAG,
                    "::filePickerResultRegister No file received"
                )
            } else {
                Log.d(
                    TAG,
                    "::filePickerResultRegister File selection failed, result code: ${result.resultCode}"
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewModel = homeViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        observeViewModel()

        binding.uploadButton.setOnClickListener {
            openFilePicker()
        }

        return binding.root
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        filePickerResultRegister.launch(intent)
    }

    private fun handleReadFile(value: FileHandlerRequestState.ReadFile) {
        HomeFragmentDirections.actionHomeFragmentToPdfReaderFragment(value.fileId).let {
            findNavController().navigate(it)
        }
        homeViewModel.handleFileHandlerRequestState()
    }

    private fun handleDownloadFile(value: FileHandlerRequestState.DownloadFile) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName.toString() + ".provider",
            value.file
        )
        val mime = "application/pdf"

        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context?.startActivity(intent)
    }

    private fun observeViewModel() {
        homeViewModel.fileHandlerRequestState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FileHandlerRequestState.ReadFile -> {
                    handleReadFile(state)
                }
                is FileHandlerRequestState.DownloadFile -> {
                    handleDownloadFile(state)
                }
                else -> {}
            }
        }
        homeViewModel.openEditFileMetadataDialog.observe(viewLifecycleOwner) { info ->
            info?.let {
                EditFileMetadataDialogFragment.newInstance(it)
                    .show(childFragmentManager, EditFileMetadataDialogFragment.TAG)
                homeViewModel.handleOpenEditFileMetadataDialogRequestState()
            }
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.canonicalName
    }
}