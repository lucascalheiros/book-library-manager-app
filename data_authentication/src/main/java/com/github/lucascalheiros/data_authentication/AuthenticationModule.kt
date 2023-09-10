package com.github.lucascalheiros.data_authentication

import com.github.lucascalheiros.data_authentication.data.repository.GoogleSignInRepositoryImpl
import com.github.lucascalheiros.data_authentication.data.repository.GuestSignInRepositoryImpl
import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.domain.repository.GuestSignInRepository
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.GuestSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignOutUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignedAccountUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.impl.GoogleSignInUseCaseImpl
import com.github.lucascalheiros.data_authentication.domain.usecase.impl.SignOutUseCaseImpl
import com.github.lucascalheiros.data_authentication.domain.usecase.impl.GuestSignInUseCaseImpl
import com.github.lucascalheiros.data_authentication.domain.usecase.impl.SignedAccountUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authenticationModuleUseCase = module {
    singleOf(::GoogleSignInUseCaseImpl) { bind<GoogleSignInUseCase>() }
    singleOf(::GoogleSignInRepositoryImpl) { bind<GoogleSignInRepository>() }
    singleOf(::SignOutUseCaseImpl) { bind<SignOutUseCase>() }
    singleOf(::GuestSignInUseCaseImpl) { bind<GuestSignInUseCase>() }
    singleOf(::SignedAccountUseCaseImpl) { bind<SignedAccountUseCase>() }
    singleOf(::GuestSignInRepositoryImpl) { bind<GuestSignInRepository>() }
}

val authenticationModules = listOf(
    authenticationModuleUseCase
)