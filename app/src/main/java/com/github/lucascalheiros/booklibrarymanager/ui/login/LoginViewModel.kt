package com.github.lucascalheiros.booklibrarymanager.ui.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.booklibrarymanager.useCase.GoogleSignInUseCase
import com.github.lucascalheiros.booklibrarymanager.useCase.SignInRequestState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch
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
                mLoginRequestState.value = LoginRequestState.Loading
                googleSignInUseCase.signIn().let {
                    when (it) {
                        is SignInRequestState.Signed -> onLoginSuccess(it.account)
                        is SignInRequestState.Unsigned -> LoginRequestState.AskUser(it.signAccountIntent)
                    }
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
    data class Success(val account: GoogleSignInAccount) : LoginRequestState()
    data class Failure(val error: Throwable? = null) : LoginRequestState()
    data class AskUser(val signInIntent: Intent) : LoginRequestState()
    object Loading : LoginRequestState()
    object Idle : LoginRequestState()
}