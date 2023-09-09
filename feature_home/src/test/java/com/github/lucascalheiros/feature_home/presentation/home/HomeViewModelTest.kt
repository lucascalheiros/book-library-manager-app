package com.github.lucascalheiros.feature_home.presentation.home

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.lucascalheiros.common_test.data.MockBookLibFilesData.items1And2
import com.github.lucascalheiros.common_test.data.MockBookLibFilesData.items2And3
import com.github.lucascalheiros.common_test.rules.MainCoroutineRule
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileListUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.mock.MockProviderRule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertContentEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : KoinTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mock(clazz.java)
    }

    @Mock
    lateinit var fileListUseCase: FileListUseCase

    @Mock
    lateinit var fileManagementUseCase: FileManagementUseCase

    @InjectMocks
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        val observer = Observer<Any> {}
        homeViewModel.tags.observeForever(observer)
        homeViewModel.filteredAndSortedFileItems.observeForever(observer)
        homeViewModel.isLoadingFiles.observeForever(observer)
    }

    @Test
    fun whenListFilesFailFilteredAndSortedFileItemsShouldNotChange() = runTest {
        `when`(fileListUseCase.listFiles()).then {
            throw Exception()
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assert(homeViewModel.filteredAndSortedFileItems.value.isNullOrEmpty())
    }

    @Test
    fun whenFileListIsLoadedFilteredAndSortedFileItemsShouldUpdateList() = runTest {
        `when`(fileListUseCase.listFiles()).then {
            items1And2
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assertContentEquals(
            items1And2.sortedByDescending { it.modifiedTime },
            homeViewModel.filteredAndSortedFileItems.value
        )
    }

    @Test
    fun whenFilteredAndSortedFileItemsShouldEmptyAtStartUp() {
        assert(homeViewModel.filteredAndSortedFileItems.value.isNullOrEmpty())
    }

    @Test
    fun whenFileListIsLoadedFilteredAndSortedFileItemsShouldChange() = runTest {
        `when`(fileListUseCase.listFiles()).then {
            items1And2
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        `when`(fileListUseCase.listFiles()).then {
            items2And3
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assertContentEquals(
            items2And3.sortedByDescending { it.modifiedTime },
            homeViewModel.filteredAndSortedFileItems.value
        )
    }

    @Test
    fun whenLoadFilesIsCalledAndIsSuccessLoadingShouldChangeItStateToTrueAndThenFalse() = runTest {
        val livedataStates = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            livedataStates.add(it)
        }
        homeViewModel.isLoadingFiles.observeForever(observer)
        `when`(fileListUseCase.listFiles()).then {
            items1And2
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assertContentEquals(
            listOf(true, false),
            livedataStates
        )
    }

    @Test
    fun whenLoadFilesIsCalledAndIsFailureLoadingShouldChangeItStateToTrueAndThenFalse() = runTest {
        val livedataStates = mutableListOf<Boolean>()
        val observer = Observer<Boolean> {
            livedataStates.add(it)
        }
        homeViewModel.isLoadingFiles.observeForever(observer)
        `when`(fileListUseCase.listFiles()).then {
            throw Exception()
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assertContentEquals(
            listOf(true, false),
            livedataStates
        )
    }

    @Test
    fun whenFileListIsUpdatedTagsShouldContainsAllTagsFromFiles() = runTest {
        `when`(fileListUseCase.listFiles()).then {
            items1And2
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assertContentEquals(
            items1And2.map { it.tags }.flatten().distinct().sortedBy { it.uppercase() },
            homeViewModel.tags.value
        )
        `when`(fileListUseCase.listFiles()).then {
            items2And3
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        assertContentEquals(
            items2And3.map { it.tags }.flatten().distinct().sortedBy { it.uppercase() },
            homeViewModel.tags.value
        )
    }

    @Test
    fun whenSelectedTagsChangeFilteredFilesShouldBeUpdated() = runTest {
        `when`(fileListUseCase.listFiles()).then {
            items1And2
        }
        homeViewModel.loadFiles()
        advanceUntilIdle()
        val selectedTag = items1And2.first().tags.first()
        homeViewModel.selectedTags.value = listOf(selectedTag)
        assertContentEquals(
            items1And2.filter { it.tags.contains(selectedTag) }.sortedByDescending { it.modifiedTime },
            homeViewModel.filteredAndSortedFileItems.value
        )
        homeViewModel.selectedTags.value = listOf()
        assertContentEquals(
            items1And2.sortedByDescending { it.modifiedTime },
            homeViewModel.filteredAndSortedFileItems.value
        )
    }

    @Test
    fun whenUploadIsSuccessfulShouldUpdateFileList() = runTest {
        val uri = mock(Uri::class.java)
        homeViewModel.uploadFile(uri)
        advanceUntilIdle()
        verify(fileListUseCase, times(1)).listFiles()
    }

    @Test
    fun whenUploadFailsShouldNotUpdateFileList() = runTest {
        val uri = mock(Uri::class.java)
        `when`(fileManagementUseCase.uploadFile(uri)).then {
            throw Exception()
        }
        homeViewModel.uploadFile(uri)
        advanceUntilIdle()
        verify(fileListUseCase, times(0)).listFiles()
    }
}