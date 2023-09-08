package com.github.lucascalheiros.data_authentication

import com.github.lucascalheiros.data_authentication.data.gateway.GoogleSignInGatewayImpl
import com.github.lucascalheiros.data_authentication.domain.gateway.GoogleSignInGateway
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.impl.GoogleSignInUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authenticationModuleUseCase = module {
    singleOf(::GoogleSignInUseCaseImpl) { bind<GoogleSignInUseCase>() }
    singleOf(::GoogleSignInGatewayImpl) { bind<GoogleSignInGateway>() }
}

val authenticationModules = listOf(
    authenticationModuleUseCase
)