package com.github.lucascalheiros.data_authentication.data.repository

import android.content.Context
import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.data_authentication.domain.GoogleSignInConfiguration.googleSignInOptions
import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.utils.toBookLibAccount
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.tasks.await

class GoogleSignInRepositoryImpl constructor(
    private val context: Context
): GoogleSignInRepository {

    private val googleSignInClient by lazy { GoogleSignIn.getClient(context, googleSignInOptions) }

    override val signedInAccount: BookLibAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)?.toBookLibAccount()

    override suspend fun trySignIn(): BookLibAccount {
        return googleSignInClient.silentSignIn().await()!!.toBookLibAccount()
    }

    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }
}