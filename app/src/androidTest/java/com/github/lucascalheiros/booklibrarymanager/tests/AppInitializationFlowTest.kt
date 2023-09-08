package com.github.lucascalheiros.booklibrarymanager.tests


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.lucascalheiros.common_test.data.MockBookLibAccountData
import com.github.lucascalheiros.booklibrarymanager.presentation.MainActivity
import com.github.lucascalheiros.common_test.rules.KoinTestRule
import com.github.lucascalheiros.booklibrarymanager.utils.ProdAppModules
import com.github.lucascalheiros.data_authentication.domain.usecase.GoogleSignInUseCase
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class AppInitializationFlowTest {

    private val googleSignInUseCase: GoogleSignInUseCase = Mockito.mock(GoogleSignInUseCase::class.java)

    private val authenticationModuleUseCase = module {
        single { googleSignInUseCase }
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = ProdAppModules.appModulesList + authenticationModuleUseCase
    )

    @Test fun whenIsLoggedShouldExhibitSplashThenHome() {
        Mockito.`when`(googleSignInUseCase.signedInAccount).thenReturn(MockBookLibAccountData.bookLibAccount1)
        Mockito.`when`(googleSignInUseCase.isUserSignedIn).thenReturn(true)

        onView(withId(com.github.lucascalheiros.feature_splash.R.id.fragment_splash_container)).check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(com.github.lucascalheiros.feature_home.R.id.fragment_home_container)).check(matches(isDisplayed()))
    }

    @Test fun whenIsNotLoggedShouldExhibitSplashThenLoginScreen() {
        Mockito.`when`(googleSignInUseCase.signedInAccount).thenReturn(null)
        Mockito.`when`(googleSignInUseCase.isUserSignedIn).thenReturn(false)

        onView(withId(com.github.lucascalheiros.feature_splash.R.id.fragment_splash_container)).check(matches(isDisplayed()))

        Thread.sleep(2000)

        onView(withId(com.github.lucascalheiros.feature_login.R.id.fragment_login_container)).check(matches(isDisplayed()))
    }
}