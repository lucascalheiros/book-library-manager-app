package com.github.lucascalheiros.feature_account

import com.github.lucascalheiros.feature_account.presentation.account.AccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val accountModule = module {
    viewModelOf(::AccountViewModel)
}

