package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.data_authentication.domain.repository.GuestSignInRepository
import com.github.lucascalheiros.data_authentication.domain.usecase.GuestSignInUseCase

class GuestSignInUseCaseImpl(
    private val guestSignInRepository: GuestSignInRepository
) : GuestSignInUseCase {

    override suspend fun signIn() {
        guestSignInRepository.updateIsGuestState(true)
    }

    companion object {
        private val TAG = GuestSignInUseCaseImpl::class.java.simpleName
    }
}
