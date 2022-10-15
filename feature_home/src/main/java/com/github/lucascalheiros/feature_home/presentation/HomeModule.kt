package com.github.lucascalheiros.feature_home.presentation

import com.github.lucascalheiros.feature_home.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

