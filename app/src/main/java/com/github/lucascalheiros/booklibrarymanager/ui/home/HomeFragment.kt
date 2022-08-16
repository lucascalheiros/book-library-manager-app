package com.github.lucascalheiros.booklibrarymanager.ui.home

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.lucascalheiros.booklibrarymanager.data.network.FileRepository
import com.github.lucascalheiros.booklibrarymanager.databinding.FragmentHomeBinding
import com.github.lucascalheiros.booklibrarymanager.utils.loadFileFromInputStream
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeName
import java.io.File
import java.io.FileReader

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()

    private val fileRepository: FileRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewModel = homeViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        observeViewModel()

        binding.uploadButton.setOnClickListener {
            openDirectory()
        }

        return binding.root
    }

    private val filePickerResultRegister = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("File selected", result.data?.data?.let {
                val contentResolver = context!!.contentResolver
                val input = contentResolver.openInputStream(it)!!
                val name = getFileName(it)!!
                val file = loadFileFromInputStream(context!!, input, name)
                MainScope().launch {
                    fileRepository.saveFile(name, file, "application/pdf")
                }
                ""
            } ?: "Empty")
        } else {
            Log.d("File selection failed, result code:", result.resultCode.toString())
        }
    }


    fun getFileName(uri: Uri): String? {
        val contentResolver = context!!.contentResolver

        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return null
    }


    fun dumpImageMetaData(uri: Uri) {
        val contentResolver = context!!.contentResolver
        // The query, because it only applies to a single document, returns only
        // one row. There's no need to filter, sort, or select fields,
        // because we want all fields for one document.
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null)

        cursor?.use {
            // moveToFirst() returns false if the cursor has 0 rows. Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (it.moveToFirst()) {

                // Note it's called "Display Name". This is
                // provider-specific, and might not necessarily be the file name.
                val displayName: String =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.i("TEST", "Display Name: $displayName")

                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
                // If the size is unknown, the value stored is null. But because an
                // int can't be null, the behavior is implementation-specific,
                // and unpredictable. So as
                // a rule, check if it's null before assigning to an int. This will
                // happen often: The storage API allows for remote files, whose
                // size might not be locally known.
                val size: String = if (!it.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    it.getString(sizeIndex)
                } else {
                    "Unknown"
                }
                Log.i("TEST", "Size: $size")
            }
        }
    }

    private fun openDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        filePickerResultRegister.launch(intent)
    }

    private fun observeViewModel() {
        homeViewModel.fileHandlerRequestState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FileHandlerRequestState.ReadFile -> {
                    HomeFragmentDirections.actionHomeFragmentToPdfReaderFragment(state.fileId).let {
                        findNavController().navigate(it)
                    }
                    homeViewModel.handleFileHandlerRequestState()
                }
            }
        }
    }
}