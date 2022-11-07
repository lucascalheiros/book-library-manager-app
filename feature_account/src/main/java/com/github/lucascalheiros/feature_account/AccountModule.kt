package com.github.lucascalheiros.feature_account

import com.github.lucascalheiros.feature_account.presentation.account.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val accountModule = module {
    viewModel { AccountViewModel(get(), get()) }
}

