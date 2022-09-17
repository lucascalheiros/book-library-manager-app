package com.github.lucascalheiros.booklibrarymanager.useCase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface GoogleSignInUseCase {
    val signedInAccount: GoogleSignInAccount?
    val isUserSignedIn: Boolean
    suspend fun signIn(): SignInRequestState
    suspend fun signOut()
}