package com.github.lucascalheiros.data_authentication.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.github.lucascalheiros.data_authentication.data.datastore.authenticationDatastore
import com.github.lucascalheiros.data_authentication.domain.repository.GuestSignInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GuestSignInRepositoryImpl constructor(
    private val context: Context
) : GuestSignInRepository {

    override val isGuestFlow: Flow<Boolean> = context.authenticationDatastore.data.map {
        it[IS_GUEST_KEY] == true
    }

    override suspend fun isGuest(): Boolean {
        return context.authenticationDatastore.data.first()[IS_GUEST_KEY] == true
    }

    override suspend fun updateIsGuestState(state: Boolean) {
        context.authenticationDatastore.edit {
            it[IS_GUEST_KEY] = state
        }
    }

    companion object {
        private val IS_GUEST_KEY = booleanPreferencesKey("IS_GUEST_KEY")
    }

}