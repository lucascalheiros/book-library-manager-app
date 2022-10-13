package com.github.lucascalheiros.data_authentication

import com.github.lucascalheiros.data_authentication.useCase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.useCase.GoogleSignInUseCaseImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authenticationModuleUseCase = module {
    single<GoogleSignInUseCase> { GoogleSignInUseCaseImpl(androidContext()) }
}

val authenticationModules = listOf(
    authenticationModuleUseCase
)