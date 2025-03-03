package com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader.adapters

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.github.lucascalheiros.common.presentation.customView.ZoomRecyclerView

import com.github.lucascalheiros.feature_pdfreader.presentation.pdfReader.handlers.ReadingPageTrackerListener
import com.github.lucascalheiros.data_drive_file.domain.utils.toBitmap
import com.github.lucascalheiros.feature_pdfreader.databinding.ItemPdfPageBinding

class PdfPageAdapter(pdfRenderer: PdfRenderer) :
    RecyclerView.Adapter<PdfPageAdapter.PdfPageViewHolder>() {

    var pdfRenderer: PdfRenderer = pdfRenderer
        set(value) {
            if (value != field) {
                field = value
                notifyDataSetChanged()
            }
        }

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

@BindingAdapter("setInitialPdfPage")
fun bind(
    rv: ZoomRecyclerView,
    pdfPage: Int
) {
    if (pdfPage >= 0) {
        rv.scrollToPosition(pdfPage)
    }
}

@BindingAdapter(
    value = ["pdfPageAdapterRenderer", "pdfPageAdapterReadingPercentageListener"],
    requireAll = false
)
fun bind(
    rv: ZoomRecyclerView,
    renderer: PdfRenderer?,
    listener: ReadingPageTrackerListener?
) {
    if (renderer == null) {
        return
    }
    if (rv.adapter !is PdfPageAdapter) {
        PdfPageAdapter(renderer).also {
            rv.adapter = it
        }
    }
    rv.addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_IDLE) {
                    val lastPosition =
                        (rv.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() + 1
                    listener?.onPageReadChange(lastPosition)
                }
            }
        })
}