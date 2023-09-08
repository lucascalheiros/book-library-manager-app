package com.github.lucascalheiros.feature_login.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignInRequestState
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
                        is SignInRequestState.Signed -> onLoginSuccess()
                        is SignInRequestState.Unsigned -> requestUserLogin()
                    }
                }
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
}

sealed class LoginRequestState {
    object Success : LoginRequestState()
    object Failure : LoginRequestState()
    object AskUser : LoginRequestState()
    object Loading : LoginRequestState()
}