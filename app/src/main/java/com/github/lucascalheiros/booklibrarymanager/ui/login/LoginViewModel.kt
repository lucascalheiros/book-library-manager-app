package com.github.lucascalheiros.booklibrarymanager.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.useCase.GoogleSignInUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val mLoginRequestState = MutableLiveData<LoginRequestState>(LoginRequestState.Idle)
    val loginRequestState: LiveData<LoginRequestState> = mLoginRequestState

    fun onLoginClick() {
        viewModelScope.launch {
            if (mLoginRequestState.value !is LoginRequestState.Loading) {
                val client = googleSignInUseCase.googleSignInClient
                mLoginRequestState.value = LoginRequestState.Loading
                try {
                    val account = client.silentSignIn().await()
                    onLoginSuccess(account)
                } catch (e: Throwable) {
                    mLoginRequestState.value = LoginRequestState.AskUser(client)
                }
            }
        }
    }

    fun onLoginFailure(error: Throwable? = null) {
        mLoginRequestState.value = LoginRequestState.Failure(error)
    }

    fun onLoginSuccess(account: GoogleSignInAccount) {
        logAccount(account)
        mLoginRequestState.value = LoginRequestState.Success(account)
    }


    private fun logAccount(account: GoogleSignInAccount) {
        Log.d(
            "[LOGIN]", "Login successful with account\n" +
                    "ID: ${account.id}\n" +
                    "Name: ${account.displayName}\n" +
                    "ServerAuth: ${account.serverAuthCode}\n" +
                    "ID Token: ${account.idToken}\n" +
                    "Email: ${account.email}\n" +
                    "PhotoUri: ${account.photoUrl}\n"
        )
    }
}

sealed class LoginRequestState {
    data class Success(val account: GoogleSignInAccount): LoginRequestState()
    data class Failure(val error: Throwable? = null): LoginRequestState()
    data class AskUser(val client: GoogleSignInClient): LoginRequestState()
    object Loading: LoginRequestState()
    object Idle: LoginRequestState()
}