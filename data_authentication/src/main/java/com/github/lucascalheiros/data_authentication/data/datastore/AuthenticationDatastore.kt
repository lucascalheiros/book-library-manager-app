package com.github.lucascalheiros.data_authentication.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val AUTHENTICATION_PREFERENCES = "AUTHENTICATION_PREFERENCES"

val Context.authenticationDatastore by preferencesDataStore(
    name = AUTHENTICATION_PREFERENCES
)
