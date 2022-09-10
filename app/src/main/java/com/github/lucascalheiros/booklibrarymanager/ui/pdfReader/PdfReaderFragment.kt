package com.github.lucascalheiros.booklibrarymanager.ui.pdfReader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.lucascalheiros.booklibrarymanager.databinding.FragmentPdfReaderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class PdfReaderFragment : Fragment() {

    private lateinit var binding: FragmentPdfReaderBinding
    private val pdfReaderViewModel: PdfReaderViewModel by viewModel()

    private val args: PdfReaderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPdfReaderBinding.inflate(inflater, container, false)

        binding.viewModel = pdfReaderViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        pdfReaderViewModel.initializeRenderer(args.fileId)

        return binding.root
    }

    override fun onDestroy() {
        pdfReaderViewModel.closeRenderer()
        super.onDestroy()
    }
}