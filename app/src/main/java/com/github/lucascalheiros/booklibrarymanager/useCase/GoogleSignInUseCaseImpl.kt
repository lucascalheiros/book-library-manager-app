package com.github.lucascalheiros.booklibrarymanager.useCase


import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class GoogleSignInUseCaseImpl constructor(
    private val context: Context
): GoogleSignInUseCase {
    private val googleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(Scopes.DRIVE_FILE))
            .build()
    }

    private val googleSignInClient by lazy { GoogleSignIn.getClient(context, googleSignInOptions) }

    override val signedInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)

    override val isUserSignedIn: Boolean
       get() = signedInAccount != null

    override suspend fun signIn(): SignInRequestState {
        return try {
            val account = googleSignInClient.silentSignIn().await()
            SignInRequestState.Signed(account)
        } catch (t: Exception) {
            SignInRequestState.Unsigned(googleSignInClient.signInIntent)
        }
    }

    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }

}

sealed class SignInRequestState {
    data class Signed(val account: GoogleSignInAccount): SignInRequestState()
    data class Unsigned(val signAccountIntent: Intent): SignInRequestState()
}