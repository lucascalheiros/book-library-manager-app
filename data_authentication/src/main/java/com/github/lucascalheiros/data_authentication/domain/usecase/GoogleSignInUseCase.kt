package com.github.lucascalheiros.data_authentication.domain.usecase

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount

interface GoogleSignInUseCase {
    val signedInAccount: BookLibAccount?
    val isUserSignedIn: Boolean
    suspend fun signIn(): SignInRequestState
    suspend fun signOut()
}

sealed class SignInRequestState {
    object Signed : SignInRequestState()
    object Unsigned : SignInRequestState()
}