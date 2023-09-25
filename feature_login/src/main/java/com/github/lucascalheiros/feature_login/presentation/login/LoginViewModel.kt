package com.github.lucascalheiros.feature_login.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.GuestSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignInRequestState
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val guestSignInUseCase: GuestSignInUseCase
) : ViewModel() {

    private val mLoginRequestState = MutableLiveData<LoginRequestState>()
    val loginRequestState: LiveData<LoginRequestState> = mLoginRequestState

    fun onGoogleSignInClick() {
        viewModelScope.launch {
            if (mLoginRequestState.value !is LoginRequestState.Loading) {
                mLoginRequestState.value = LoginRequestState.Loading
                googleSignInUseCase.signIn().let {
                    when (it) {
                        is SignInRequestState.Signed -> onLoginSuccess()
                        is SignInRequestState.Unsigned -> requestUserLogin()
                    }
                }
            }
        }
    }

    fun onEnterAsGuestClick() {
        viewModelScope.launch {
            try {
                guestSignInUseCase.signIn()
                onLoginSuccess()
            } catch (e: Exception) {
                logError(TAG, "::onEnterAsGuestClick", e)
                onLoginFailure()
            }
        }
    }

    fun onLoginFailure() {
        mLoginRequestState.value = LoginRequestState.Failure
    }

    fun onLoginSuccess() {
        mLoginRequestState.value = LoginRequestState.Success
    }

    private fun requestUserLogin() {
        mLoginRequestState.value = LoginRequestState.AskUser
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }
}

sealed class LoginRequestState {
    object Success : LoginRequestState()
    object Failure : LoginRequestState()
    object AskUser : LoginRequestState()
    object Loading : LoginRequestState()
}