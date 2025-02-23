package com.github.lucascalheiros.feature_home.presentation.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.common.navigation.NavigationRoutes
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_drive_file.domain.utils.MimeTypeConstants
import com.github.lucascalheiros.feature_home.R
import com.github.lucascalheiros.feature_home.databinding.FragmentHomeBinding
import com.github.lucascalheiros.feature_home.presentation.editFileMetadata.EditFileMetadataDialogFragment
import com.github.service_synchronization.notifications.DownloadNotificationIdGenerator
import com.github.service_synchronization.notifications.dismissCurrentDownloadNotification
import com.github.service_synchronization.notifications.showDownloadNotification
import com.github.service_synchronization.worker.FileSynchronizationWorker
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()

    private val filePickerResultRegister =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { homeViewModel.uploadFile(it) } ?: logError(
                    listOf(
                        LogTags.FILE_PICKER,
                        TAG
                    ),
                    "::filePickerResultRegister No file received"
                )
            } else {
                logError(
                    listOf(
                        LogTags.FILE_PICKER,
                        TAG
                    ),
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

        setupUiListeners()

        loadData()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForGoogleSignInResult.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        FileSynchronizationWorker.startWorker(requireContext())

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!homeViewModel.shouldInterceptBackPressed()) {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    private val startForGoogleSignInResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
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

                is FileHandlerRequestState.Loading -> {
                    context?.showDownloadNotification(DownloadNotificationIdGenerator.iterateNext(),null, false)
                }

                is FileHandlerRequestState.Failure -> {
                    context?.dismissCurrentDownloadNotification()
                    Toast.makeText(context, getString(R.string.file_download_failed), Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
            if (state != FileHandlerRequestState.Idle && state !is FileHandlerRequestState.Loading) {
                homeViewModel.handleFileHandlerRequestState()
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

    private fun setupUiListeners() {
        binding.uploadButton.setOnClickListener {
            openFilePicker()
        }
    }

    private fun loadData() {
        homeViewModel.loadFiles()
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = MimeTypeConstants.pdf
        }
        filePickerResultRegister.launch(intent)
    }

    private fun handleReadFile(value: FileHandlerRequestState.ReadFile) {
        NavDeepLinkRequest.Builder
            .fromUri(NavigationRoutes.pdfReader(value.fileId, value.page))
            .build().let {
                findNavController().navigate(it)
            }
    }

    private fun handleDownloadFile(value: FileHandlerRequestState.DownloadFile) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName.toString() + ".provider",
            value.file
        )
        val mime = MimeTypeConstants.pdf

        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context?.showDownloadNotification(DownloadNotificationIdGenerator.currentId(), intent, true)
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}