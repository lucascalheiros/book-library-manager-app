package com.github.lucascalheiros.data_authentication.useCase


import android.content.Context
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logDebug
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.tasks.await


class GoogleSignInUseCaseImpl constructor(
    private val context: Context
) : GoogleSignInUseCase {
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
            logDebug(
                listOf(LogTags.LOGIN, TAG),
                "GoogleSignInUseCaseImpl::signIn successful ${account.email}"
            )
            SignInRequestState.Signed(account)
        } catch (t: Exception) {
            logDebug(
                listOf(LogTags.LOGIN, TAG),
                "GoogleSignInUseCaseImpl::signIn silent sign in request failed, trying to sign in with intent",
                t
            )
            SignInRequestState.Unsigned(googleSignInClient.signInIntent)
        }
    }

    override suspend fun signOut() {
        googleSignInClient.signOut().await()
    }

    companion object {
        private val TAG = GoogleSignInUseCaseImpl::class.java.canonicalName
    }
}
