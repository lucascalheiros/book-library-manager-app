package com.github.lucascalheiros.data_authentication.domain.usecase

import com.github.lucascalheiros.common.model.interfaces.BookLibAccount

interface SignedAccountUseCase {
    val signedInAccount: BookLibAccount?
    val isUserSignedIn: Boolean
}
