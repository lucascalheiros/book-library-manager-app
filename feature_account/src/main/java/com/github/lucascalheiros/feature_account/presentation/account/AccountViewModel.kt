package com.github.lucascalheiros.feature_account.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.common.utils.constants.LogTags
import com.github.lucascalheiros.common.utils.logError
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import com.github.lucascalheiros.data_drive_file.useCase.FileListUseCase
import kotlinx.coroutines.launch

class AccountViewModel(
    private val fileListUseCase: FileListUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val mLogoutEvent = MutableLiveData<LogoutRequestState>(LogoutRequestState.Idle)
    val logoutEvent: LiveData<LogoutRequestState> = mLogoutEvent

    private val mAccount = MutableLiveData<BookLibAccount>()
    val account: LiveData<BookLibAccount> = mAccount

    init {
        val account = googleSignInUseCase.signedInAccount
        if (account == null) {
            mLogoutEvent.value = LogoutRequestState.Success
        } else {
            mAccount.value = account
        }
    }

    fun downloadData() {
        // TODO service to proceed with the download
    }

    fun logout() {
        viewModelScope.launch {
            try {
                if (mLogoutEvent.value is LogoutRequestState.Loading) {
                    return@launch
                }
                mLogoutEvent.value = LogoutRequestState.Loading
                googleSignInUseCase.signOut()
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
}

sealed class LogoutRequestState {
    object Success : LogoutRequestState()
    object Loading : LogoutRequestState()
    object Failure : LogoutRequestState()
    object Idle : LogoutRequestState()
}