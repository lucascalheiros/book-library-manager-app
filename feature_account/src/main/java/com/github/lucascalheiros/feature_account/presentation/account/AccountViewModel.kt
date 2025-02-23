package com.github.lucascalheiros.feature_account.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logDebug
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignOutUseCase
import com.github.lucascalheiros.data_authentication.domain.usecase.SignedAccountUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class AccountViewModel(
    private val signedAccountUseCase: SignedAccountUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val mLogoutEvent = MutableLiveData<LogoutRequestState>(LogoutRequestState.Idle)
    val logoutEvent: LiveData<LogoutRequestState> = mLogoutEvent

    private val mLinkWithGoogleEvent = MutableSharedFlow<Unit>()
    val linkWithGoogleEvent: SharedFlow<Unit> = mLinkWithGoogleEvent

    private val mAccount =
        signedAccountUseCase.signedInAccountFlow.filterNotNull().asLiveData(viewModelScope.coroutineContext)
    val photoUrl = mAccount.map { it.photoUrl }
    val name = mAccount.map { it.name }
    val email = mAccount.map { it.email }
    val isGuest = signedAccountUseCase.isGuestUserFlow.asLiveData(viewModelScope.coroutineContext)

    fun updateAccountInfo() {
        signedAccountUseCase.signedInAccount
    }

    fun linkWithGoogle() {
        viewModelScope.launch {
            mLinkWithGoogleEvent.emit(Unit)
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                if (mLogoutEvent.value is LogoutRequestState.Loading) {
                    return@launch
                }
                mLogoutEvent.value = LogoutRequestState.Loading
                signOutUseCase.signOut()
                mLogoutEvent.value = LogoutRequestState.Success
            } catch (error: Exception) {
                logError(
                    listOf(LogTags.LOGOUT),
                    "::onLoginFailure Unable to login",
                    error
                )
                mLogoutEvent.value = LogoutRequestState.Failure
            }
        }
    }

    fun handleLogoutEvent() {
        mLogoutEvent.value = LogoutRequestState.Idle
    }

    fun onLinkWithGoogleSuccess() {
        viewModelScope.launch {
            try {
                googleSignInUseCase.signIn()
                signedAccountUseCase.signedInAccount
                logDebug(GOOGLE_SIGN_IN_TAGS, "::onLinkWithGoogleSuccess")
            } catch (e: Exception) {
                logError(GOOGLE_SIGN_IN_TAGS, "::onLinkWithGoogleSuccess", e)
            }
        }
    }

    companion object {
        private val TAG = AccountViewModel::class.java.simpleName
        private val GOOGLE_SIGN_IN_TAGS = listOf(LogTags.LOGIN, LogTags.GOOGLE, TAG)
    }
}

sealed class LogoutRequestState {
    object Success : LogoutRequestState()
    object Loading : LogoutRequestState()
    object Failure : LogoutRequestState()
    object Idle : LogoutRequestState()
}