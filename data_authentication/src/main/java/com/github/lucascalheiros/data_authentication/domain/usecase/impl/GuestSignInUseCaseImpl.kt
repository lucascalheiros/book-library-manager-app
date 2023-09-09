package com.github.lucascalheiros.data_authentication.domain.usecase.impl

import com.github.lucascalheiros.data_authentication.domain.usecase.GuestSignInUseCase

class GuestSignInUseCaseImpl : GuestSignInUseCase {

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    companion object {
        private val TAG = GuestSignInUseCaseImpl::class.java.simpleName
    }
}
