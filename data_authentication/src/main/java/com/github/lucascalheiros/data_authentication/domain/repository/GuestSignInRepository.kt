package com.github.lucascalheiros.data_authentication.domain.repository

import kotlinx.coroutines.flow.Flow

interface GuestSignInRepository {
    val isGuestFlow: Flow<Boolean>
    suspend fun isGuest(): Boolean
    suspend fun updateIsGuestState(state: Boolean)
}