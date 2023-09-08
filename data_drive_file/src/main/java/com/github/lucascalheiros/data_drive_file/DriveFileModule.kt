package com.github.lucascalheiros.data_drive_file

import android.app.Application
import androidx.room.Room
import com.github.lucascalheiros.data_drive_file.data.network.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.data.network.DriveFileRepositoryImpl
import com.github.lucascalheiros.data_drive_file.data.storage.database.AppDatabase
import com.github.lucascalheiros.data_drive_file.usecase.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val driveFileModuleData = module {
    single<DriveFileRepository> { DriveFileRepositoryImpl(androidContext()) }
}

val driveFileModuleUseCase = module {
    single<FileCacheUseCase> { FileCacheUseCaseImpl(get()) }
    single<FileListUseCase> { FileListUseCaseImpl(get(), get()) }
    single<FileManagementUseCase> { FileManagementUseCaseImpl(get(), get()) }
    single<ReadPdfUseCase> { ReadPdfUseCaseImpl(get(), get()) }
}

val driveFileModuleDb = module {
    fun provideDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "AppDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { provideDataBase(androidApplication()) }
}

val driveFileModules = listOf(
    driveFileModuleData,
    driveFileModuleDb,
    driveFileModuleUseCase
)