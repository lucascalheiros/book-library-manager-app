package com.github.lucascalheiros.data_authentication.useCase

import android.content.Intent
import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleSignInUseCase {
    val signedInAccount: BookLibAccount?
    val isUserSignedIn: Boolean
    suspend fun signIn(): SignInRequestState
    suspend fun signOut()
}

sealed class SignInRequestState {
    data class Signed(val account: GoogleSignInAccount) : SignInRequestState()
    data class Unsigned(val signAccountIntent: Intent) : SignInRequestState()
}