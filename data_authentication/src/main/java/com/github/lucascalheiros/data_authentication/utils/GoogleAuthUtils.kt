package com.github.lucascalheiros.data_authentication.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

fun GoogleSignInAccount.infoString(): String {
    return "ID: ${id}\n" +
            "Name: ${displayName}\n" +
            "ServerAuth: ${serverAuthCode}\n" +
            "ID Token: ${idToken}\n" +
            "Email: ${email}\n" +
            "PhotoUri: ${photoUrl}\n"
}