package com.github.lucascalheiros.data_authentication.domain.repository

import com.github.lucascalheiros.data_authentication.domain.model.BookLibAccount
import kotlinx.coroutines.flow.Flow
import kotlin.jvm.Throws

interface GoogleSignInRepository {
    val signedInAccountFlow: Flow<BookLibAccount?>
    val signedInAccount: BookLibAccount?
    @Throws(Exception::class)
    suspend fun trySignIn(): BookLibAccount
    suspend fun signOut()
}