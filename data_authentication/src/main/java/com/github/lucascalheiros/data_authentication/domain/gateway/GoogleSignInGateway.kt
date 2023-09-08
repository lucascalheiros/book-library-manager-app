package com.github.lucascalheiros.data_authentication.domain.gateway

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import kotlin.jvm.Throws

interface GoogleSignInGateway {
    val signedInAccount: BookLibAccount?
    @Throws(Exception::class)
    suspend fun trySignIn(): BookLibAccount
    suspend fun signOut()
}