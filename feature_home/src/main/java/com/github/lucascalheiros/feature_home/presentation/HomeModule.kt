package com.github.lucascalheiros.feature_home.presentation

import com.github.lucascalheiros.feature_home.presentation.home.HomeViewModel
import com.github.lucascalheiros.feature_home.presentation.editFileMetadata.EditFileMetadataDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::EditFileMetadataDialogViewModel)
}

