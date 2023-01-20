package com.github.lucascalheiros.booklibrarymanager.tests

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.github.lucascalheiros.common_test.rules.KoinTestRule
import com.github.lucascalheiros.booklibrarymanager.utils.ProdAppModules
import com.github.lucascalheiros.common_test.data.MockBookLibFilesData
import com.github.lucascalheiros.data_drive_file.useCase.FileListUseCase
import com.github.lucascalheiros.feature_home.presentation.home.HomeFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {

    private val fileListUseCase: FileListUseCase = Mockito.mock(FileListUseCase::class.java)

    private val mockModule = module {
        single { fileListUseCase }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = ProdAppModules.appModulesList + mockModule
    )

    @Test
    fun whenOpenHomeAllItemsShouldBeDisplayed() {
        runBlocking {
            Mockito.`when`(fileListUseCase.listFiles()).thenReturn(MockBookLibFilesData.all)
        }
        launchFragmentInContainer<HomeFragment>(themeResId = com.github.lucascalheiros.common.R.style.Theme_BookLibraryManager)
        MockBookLibFilesData.all.forEach {
            onView(withText(it.name)).check(
                ViewAssertions.matches(isDisplayed())
            )
        }
    }

    @Test
    fun whenOpenFilterMenuAllTagsShouldBeDisplayed() {
        runBlocking {
            Mockito.`when`(fileListUseCase.listFiles()).thenReturn(MockBookLibFilesData.all)
            launchFragmentInContainer<HomeFragment>(themeResId = com.github.lucascalheiros.common.R.style.Theme_BookLibraryManager)
            onView(withId(com.github.lucascalheiros.feature_home.R.id.btnFilter)).perform(click())
            delay(1000)
            MockBookLibFilesData.all.map { it.tags }.flatten().forEach {
                onView(withText(it)).check(
                    ViewAssertions.matches(isDisplayed())
                )
            }
        }
    }

}