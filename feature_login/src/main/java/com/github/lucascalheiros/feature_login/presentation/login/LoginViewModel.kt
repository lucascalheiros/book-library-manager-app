package com.github.lucascalheiros.feature_login.presentation.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.useCase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.useCase.SignInRequestState
import com.github.lucascalheiros.data_authentication.utils.infoString
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.launch

class LoginViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val mLoginRequestState = MutableLiveData<LoginRequestState>()
    val loginRequestState: LiveData<LoginRequestState> = mLoginRequestState

    fun onLoginClick() {
        viewModelScope.launch {
            if (mLoginRequestState.value !is LoginRequestState.Loading) {
                mLoginRequestState.value = LoginRequestState.Loading
                googleSignInUseCase.signIn().let {
                    when (it) {
                        is SignInRequestState.Signed -> onLoginSuccess(it.account)
                        is SignInRequestState.Unsigned -> requestUserLogin(it.signAccountIntent)
                    }
                }
            }
        }
    }

    fun onLoginFailure(error: Exception? = null) {
        logError(
            listOf(LogTags.LOGIN, TAG),
            "::onLoginFailure Unable to login",
            error
        )
        mLoginRequestState.value = LoginRequestState.Failure(error)
    }

    fun onLoginSuccess(account: GoogleSignInAccount) {
        logDebug(
            listOf(LogTags.LOGIN, TAG),
            "Login successful with account\n" + account.infoString()
        )
        mLoginRequestState.value = LoginRequestState.Success(account)
    }

    private fun requestUserLogin(signAccountIntent: Intent) {
        mLoginRequestState.value = LoginRequestState.AskUser(signAccountIntent)
    }

    companion object {
        private val TAG = LoginViewModel::class.java.canonicalName
    }
}

sealed class LoginRequestState {
    data class Success(val account: GoogleSignInAccount) : LoginRequestState()
    data class Failure(val error: Exception? = null) : LoginRequestState()
    data class AskUser(val signInIntent: Intent) : LoginRequestState()
    object Loading : LoginRequestState()
}