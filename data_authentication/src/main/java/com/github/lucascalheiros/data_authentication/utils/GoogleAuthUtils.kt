package com.github.lucascalheiros.data_authentication.utils

import com.github.lucascalheiros.data_authentication.domain.model.BookLibAccount
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

fun GoogleSignInAccount.infoString(): String {
    return "ID: ${id}\n" +
            "Name: ${displayName}\n" +
            "ServerAuth: ${serverAuthCode}\n" +
            "ID Token: ${idToken}\n" +
            "Email: ${email}\n" +
            "PhotoUri: ${photoUrl}\n"
}

fun GoogleSignInAccount.toBookLibAccount(): BookLibAccount {
    return object : BookLibAccount {
        override val id: String?
            get() = this@toBookLibAccount.id
        override val name: String?
            get() = displayName
        override val photoUrl: String?
            get() = this@toBookLibAccount.photoUrl?.toString()
        override val email: String?
            get() = this@toBookLibAccount.email
    }
}