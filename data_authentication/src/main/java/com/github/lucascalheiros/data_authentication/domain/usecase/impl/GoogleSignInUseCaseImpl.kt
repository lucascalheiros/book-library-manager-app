package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.data_authentication.domain.gateway.GoogleSignInGateway
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignInRequestState

class GoogleSignInUseCaseImpl constructor(
    private val googleSignInGateway: GoogleSignInGateway
) : GoogleSignInUseCase {


    override val signedInAccount: BookLibAccount?
        get() = googleSignInGateway.signedInAccount

    override val isUserSignedIn: Boolean
        get() = signedInAccount != null

    override suspend fun signIn(): SignInRequestState {
        return try {
            val account = googleSignInGateway.trySignIn()
            logDebug(
                TAG_LIST,
                "GoogleSignInUseCaseImpl::signIn successful ${account.email}"
            )
            SignInRequestState.Signed
        } catch (t: Exception) {
            logDebug(
                TAG_LIST,
                "GoogleSignInUseCaseImpl::signIn silent sign in request failed, trying to sign in with intent",
                t
            )
            SignInRequestState.Unsigned
        }
    }

    override suspend fun signOut() {
        googleSignInGateway.signOut()
    }

    companion object {
        private val TAG = GoogleSignInUseCaseImpl::class.java.simpleName
        private val TAG_LIST = listOf(LogTags.LOGIN, TAG)
    }
}
