package com.github.lucascalheiros.feature_login

import com.github.lucascalheiros.feature_login.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val loginModule = module {
    viewModelOf(::LoginViewModel)
}

