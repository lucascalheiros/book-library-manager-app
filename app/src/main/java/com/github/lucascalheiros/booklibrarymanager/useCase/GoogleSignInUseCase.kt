package com.github.lucascalheiros.booklibrarymanager.useCase


import android.content.Context
import com.github.lucascalheiros.booklibrarymanager.utils.GOOGLE_OAUTH_SERVER_CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import org.koin.core.annotation.Single

@Single
class GoogleSignInUseCase constructor(
    private val context: Context
) {
    private val googleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_FILE))
            .build()
    }

    val googleSignInClient by lazy { GoogleSignIn.getClient(context, googleSignInOptions) }

    val lastSignedInAccount: GoogleSignInAccount?
        get() = GoogleSignIn.getLastSignedInAccount(context)


    fun isUserSignedIn(): Boolean {
        return lastSignedInAccount != null
    }

    fun signOut() {
        googleSignInClient.signOut()
    }

}