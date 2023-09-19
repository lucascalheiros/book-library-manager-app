package com.github.lucascalheiros.data_authentication.domain.usecase

import com.github.lucascalheiros.data_authentication.domain.model.BookLibAccount
import kotlinx.coroutines.flow.Flow

interface SignedAccountUseCase {
    val signedInAccount: BookLibAccount?
    val signedInAccountFlow: Flow<BookLibAccount?>
    val isGuestUserFlow: Flow<Boolean>
    val isUserSignedInFlow: Flow<Boolean>
    suspend fun isGuestUser(): Boolean
    suspend fun isUserSignedIn(): Boolean
}
