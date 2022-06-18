package com.github.lucascalheiros.booklibrarymanager.ui.pdfReader

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.lucascalheiros.booklibrarymanager.adapters.PdfPageAdapter
import com.github.lucascalheiros.booklibrarymanager.databinding.FragmentPdfReaderBinding
import com.github.lucascalheiros.booklibrarymanager.utils.loadParcelFileDescriptorFromAsset


class PdfReaderFragment : Fragment() {

    private lateinit var renderer: PdfRenderer
    private lateinit var binding: FragmentPdfReaderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfReaderBinding.inflate(inflater, container, false)

        renderer =
            PdfRenderer(loadParcelFileDescriptorFromAsset(requireContext(), TEST_PDF_ASSET))

        binding.pageList.adapter = PdfPageAdapter(renderer)

        return binding.root
    }

    override fun onDestroy() {
        renderer.close()
        super.onDestroy()
    }

    companion object {
        const val TEST_PDF_ASSET = ""
    }
}