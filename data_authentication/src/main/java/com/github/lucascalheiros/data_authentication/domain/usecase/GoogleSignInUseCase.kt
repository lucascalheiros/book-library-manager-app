package com.github.lucascalheiros.data_authentication.domain.usecase

interface GoogleSignInUseCase {
    suspend fun signIn(): SignInRequestState
}

sealed class SignInRequestState {
    object Signed : SignInRequestState()
    object Unsigned : SignInRequestState()
}