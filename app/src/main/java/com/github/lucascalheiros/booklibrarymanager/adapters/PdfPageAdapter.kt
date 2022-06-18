package com.github.lucascalheiros.booklibrarymanager.adapters

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.lucascalheiros.booklibrarymanager.databinding.ItemPdfPageBinding
import com.github.lucascalheiros.booklibrarymanager.utils.toBitmap

class PdfPageAdapter(private val pdfRenderer: PdfRenderer) :
    RecyclerView.Adapter<PdfPageAdapter.PdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        val binding = ItemPdfPageBinding.inflate(LayoutInflater.from(parent.context))
        return PdfPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        val page: PdfRenderer.Page = pdfRenderer.openPage(position)

        holder.bind(page.toBitmap())
    }

    override fun getItemCount(): Int {
        return pdfRenderer.pageCount
    }

    class PdfPageViewHolder(
        private val binding: ItemPdfPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bitmap: Bitmap) {
            binding.pdfPage.setImageBitmap(bitmap)
        }
    }

}