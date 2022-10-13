package com.github.lucascalheiros.booklibrarymanager.ui.dialogs.editFileMetadata.model

import android.os.Parcelable
import com.github.lucascalheiros.commom.interfaces.BookLibFile
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class EditFileMetadataDialogInfo(
    val file: @RawValue BookLibFile,
    val tagOptions: List<String>,
) : Parcelable
