package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.domain.usecase.SignedAccountUseCase

class SignedAccountUseCaseImpl(
    private val googleSignInRepository: GoogleSignInRepository
) : SignedAccountUseCase {

    override val signedInAccount: BookLibAccount?
        get() = googleSignInRepository.signedInAccount

    override val isUserSignedIn: Boolean
        get() = signedInAccount != null

}