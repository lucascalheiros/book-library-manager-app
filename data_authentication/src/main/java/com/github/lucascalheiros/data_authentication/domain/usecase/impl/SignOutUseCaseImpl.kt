package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.data_authentication.domain.repository.GoogleSignInRepository
import com.github.lucascalheiros.data_authentication.domain.repository.GuestSignInRepository
import com.github.lucascalheiros.data_authentication.domain.usecase.SignOutUseCase

class SignOutUseCaseImpl(
    private val googleSignInRepository: GoogleSignInRepository,
    private val guestSignInRepository: GuestSignInRepository
): SignOutUseCase {

    override suspend fun signOut() {
        googleSignInRepository.signOut()
        guestSignInRepository.updateIsGuestState(false)
    }

}
