package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.domain.repository.GuestSignInRepository
import com.github.lucascalheiros.data_authentication.domain.usecase.SignedAccountUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SignedAccountUseCaseImpl(
    private val googleSignInRepository: GoogleSignInRepository,
    private val guestSignInRepository: GuestSignInRepository
) : SignedAccountUseCase {

    override val signedInAccount: BookLibAccount?
        get() = googleSignInRepository.signedInAccount

    override val signedInAccountFlow: Flow<BookLibAccount?>
        get() = googleSignInRepository.signedInAccountFlow

    override val isGuestUserFlow: Flow<Boolean>
        get() = googleSignInRepository.signedInAccountFlow.combine(guestSignInRepository.isGuestFlow) { account, isGuest ->
            account == null && isGuest
        }

    override val isUserSignedInFlow: Flow<Boolean>
        get() = googleSignInRepository.signedInAccountFlow.combine(guestSignInRepository.isGuestFlow) { account, isGuest ->
            account != null || isGuest
        }
    override suspend fun isUserSignedIn(): Boolean {
        return signedInAccount != null || guestSignInRepository.isGuest()
    }

    override suspend fun isGuestUser(): Boolean {
        return guestSignInRepository.isGuest()
    }

}