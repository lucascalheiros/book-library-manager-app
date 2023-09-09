package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.data_authentication.domain.gateway.GoogleSignInGateway
import com.github.lucascalheiros.data_authentication.domain.usecase.SignOutUseCase

class SignOutUseCaseImpl(
    private val googleSignInGateway: GoogleSignInGateway
): SignOutUseCase {

    override suspend fun signOut() {
        googleSignInGateway.signOut()
    }

}
