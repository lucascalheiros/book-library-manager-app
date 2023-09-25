package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignInRequestState

class GoogleSignInUseCaseImpl constructor(
    private val googleSignInRepository: GoogleSignInRepository
) : GoogleSignInUseCase {

    override suspend fun signIn(): SignInRequestState {
        return try {
            val account = googleSignInRepository.trySignIn()
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

    companion object {
        private val TAG = GoogleSignInUseCaseImpl::class.java.simpleName
        private val TAG_LIST = listOf(LogTags.LOGIN, TAG)
    }
}
