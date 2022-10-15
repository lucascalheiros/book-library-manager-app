package com.github.lucascalheiros.booklibrarymanager.presentation.login

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.lucascalheiros.booklibrarymanager.rules.MainCoroutineRule
import com.github.lucascalheiros.data_authentication.useCase.GoogleSignInUseCase
import com.github.lucascalheiros.data_authentication.useCase.SignInRequestState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest : KoinTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mock(clazz.java)
    }

    @Mock
    lateinit var googleSignInUseCase: GoogleSignInUseCase

    @InjectMocks
    lateinit var loginViewModel: com.github.lucascalheiros.feature_login.presentation.login.LoginViewModel

    @Test
    fun whenUserIsSignedLoginRequestShouldSucceed() = runTest {
        val account = mock(GoogleSignInAccount::class.java)
        `when`(googleSignInUseCase.signIn()).thenReturn(SignInRequestState.Signed(account))
        loginViewModel.onLoginClick()
        advanceUntilIdle()
        assert(loginViewModel.loginRequestState.value is com.github.lucascalheiros.feature_login.presentation.login.LoginRequestState.Success)
    }

    @Test
    fun whenUserIsSignedLoginRequestShouldReturnSignedAccount() = runTest {
        val account = mock(GoogleSignInAccount::class.java)
        `when`(googleSignInUseCase.signIn()).thenReturn(SignInRequestState.Signed(account))
        loginViewModel.onLoginClick()
        advanceUntilIdle()
        assertEquals((loginViewModel.loginRequestState.value as com.github.lucascalheiros.feature_login.presentation.login.LoginRequestState.Success).account, account)
    }

    @Test
    fun whenUserIsNotSignedLoginRequestShouldAskForUserToLogin() = runTest {
        val intent = mock(Intent::class.java)
        `when`(googleSignInUseCase.signIn()).thenReturn(SignInRequestState.Unsigned(intent))
        loginViewModel.onLoginClick()
        advanceUntilIdle()
        assert(loginViewModel.loginRequestState.value is com.github.lucascalheiros.feature_login.presentation.login.LoginRequestState.AskUser)
    }

    @Test
    fun whenUserIsNotSignedLoginRequestShouldAskForUserToLoginWithIntent() = runTest {
        val intent = mock(Intent::class.java)
        `when`(googleSignInUseCase.signIn()).thenReturn(SignInRequestState.Unsigned(intent))
        loginViewModel.onLoginClick()
        advanceUntilIdle()
        assertEquals((loginViewModel.loginRequestState.value as com.github.lucascalheiros.feature_login.presentation.login.LoginRequestState.AskUser).signInIntent, intent)
    }

}


