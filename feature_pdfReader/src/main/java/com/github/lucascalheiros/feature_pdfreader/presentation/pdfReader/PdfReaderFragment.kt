package com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.lucascalheiros.feature_pdfreader.databinding.FragmentPdfReaderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PdfReaderFragment : Fragment() {

    private lateinit var binding: FragmentPdfReaderBinding
    private val pdfReaderViewModel: PdfReaderViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPdfReaderBinding.inflate(inflater, container, false)

        binding.viewModel = pdfReaderViewModel

        binding.lifecycleOwner = viewLifecycleOwner


        pdfReaderViewModel.initializeRenderer(
            arguments?.getString("fileId").orEmpty(), arguments?.getString("initialPage")
                ?.toIntOrNull()
                ?: 0
        )

        return binding.root
    }

    override fun onDestroy() {
        val isChangingConfigurations = activity?.isChangingConfigurations == true
        if (!isChangingConfigurations) {
            pdfReaderViewModel.closeRenderer()
        }
        super.onDestroy()
    }
}