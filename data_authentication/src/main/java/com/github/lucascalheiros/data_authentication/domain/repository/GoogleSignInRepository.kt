package com.github.lucascalheiros.data_authentication.domain.repository

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import kotlin.jvm.Throws

interface GoogleSignInRepository {
    val signedInAccount: BookLibAccount?
    @Throws(Exception::class)
    suspend fun trySignIn(): BookLibAccount
    suspend fun signOut()
}