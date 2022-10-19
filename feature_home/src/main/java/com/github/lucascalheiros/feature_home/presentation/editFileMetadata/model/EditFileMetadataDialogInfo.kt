package com.github.lucascalheiros.feature_home.presentation.editFileMetadata.model

import android.os.Parcelable
import com.github.lucascalheiros.common.interfaces.BookLibFile
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class EditFileMetadataDialogInfo(
    val file: @RawValue BookLibFile,
    val tagOptions: List<String>,
) : Parcelable
