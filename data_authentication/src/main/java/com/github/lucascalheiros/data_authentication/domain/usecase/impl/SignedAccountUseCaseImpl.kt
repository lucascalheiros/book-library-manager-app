package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.data_authentication.domain.gateway.GoogleSignInGateway
import com.github.lucascalheiros.data_authentication.domain.usecase.SignedAccountUseCase

class SignedAccountUseCaseImpl(
    private val googleSignInGateway: GoogleSignInGateway
) : SignedAccountUseCase {

    override val signedInAccount: BookLibAccount?
        get() = googleSignInGateway.signedInAccount

    override val isUserSignedIn: Boolean
        get() = signedInAccount != null

}