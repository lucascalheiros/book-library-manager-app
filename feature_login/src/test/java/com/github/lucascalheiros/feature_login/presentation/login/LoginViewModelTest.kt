package com.github.lucascalheiros.feature_login.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.lucascalheiros.common.model.interfaces.BookLibAccount
import com.github.lucascalheiros.common_test.rules.MainCoroutineRule
import com.github.lucascalheiros.data_authentication.domain.gateway.GoogleSignInGateway
import com.github.lucascalheiros.data_authentication.domain.usecase.impl.GoogleSignInUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


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
    lateinit var googleSignInGateway: GoogleSignInGateway

    @InjectMocks
    lateinit var googleSignInUseCase: GoogleSignInUseCaseImpl

    val loginViewModel: LoginViewModel by lazy {
        LoginViewModel(googleSignInUseCase)
    }

    @Test
    fun whenUserIsSignedLoginRequestShouldSucceed() = runTest {
        val account = mock(BookLibAccount::class.java)
        `when`(googleSignInGateway.trySignIn()).thenReturn(account)
        loginViewModel.onGoogleSignInClick()
        advanceUntilIdle()
        assertEquals(
            LoginRequestState.Success,
            loginViewModel.loginRequestState.value
        )
    }

    @Test
    fun whenUserIsNotSignedLoginRequestShouldAskForUserToLogin() = runTest {
        `when`(googleSignInGateway.trySignIn()).thenThrow(Exception())
        loginViewModel.onGoogleSignInClick()
        advanceUntilIdle()
        assertEquals(
            LoginRequestState.AskUser,
            loginViewModel.loginRequestState.value
        )
    }

    @Test
    fun whenLoginFailedModelStateShouldBeFailure() = runTest {
        loginViewModel.onLoginFailure()
        advanceUntilIdle()
        assertEquals(
            LoginRequestState.Failure,
            loginViewModel.loginRequestState.value
        )
    }

}


