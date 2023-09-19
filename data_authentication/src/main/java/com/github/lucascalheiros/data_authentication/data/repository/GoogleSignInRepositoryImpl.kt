package com.github.lucascalheiros.data_authentication.data.repository

import android.content.Context
import com.github.lucascalheiros.data_authentication.domain.model.BookLibAccount
import com.github.lucascalheiros.data_authentication.domain.GoogleSignInConfiguration.googleSignInOptions
import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.utils.toBookLibAccount
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.tasks.await

class GoogleSignInRepositoryImpl constructor(
    private val context: Context
) : GoogleSignInRepository {

    private val googleSignInClient by lazy { GoogleSignIn.getClient(context, googleSignInOptions) }

    private val mSignedInAccountFlow = MutableSharedFlow<BookLibAccount?>(replay = 1)
    override val signedInAccountFlow: Flow<BookLibAccount?> = mSignedInAccountFlow

    override val signedInAccount: BookLibAccount?
        get() {
            return GoogleSignIn.getLastSignedInAccount(context)?.toBookLibAccount().also {
                mSignedInAccountFlow.tryEmit(it)
            }
        }

    override suspend fun trySignIn(): BookLibAccount {
        return googleSignInClient.silentSignIn().await()!!.toBookLibAccount().also {
            signedInAccount
        }
    }

    override suspend fun signOut() {
        googleSignInClient.signOut().await()
        signedInAccount
    }
}