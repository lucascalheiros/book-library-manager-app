package com.github.lucascalheiros.data_drive_file

import android.app.Application
import androidx.room.Room
import com.github.lucascalheiros.data_drive_file.domain.network.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.data.network.DriveFileRepositoryImpl
import com.github.lucascalheiros.data_drive_file.data.storage.database.AppDatabase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileCacheUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.FileCacheUseCaseImpl
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileListUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.FileListUseCaseImpl
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.FileManagementUseCaseImpl
import com.github.lucascalheiros.data_drive_file.domain.usecase.ReadPdfUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.ReadPdfUseCaseImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val driveFileModuleData = module {
    singleOf(::DriveFileRepositoryImpl) { bind<DriveFileRepository>() }
}

val driveFileModuleUseCase = module {
    singleOf(::FileCacheUseCaseImpl) { bind<FileCacheUseCase>() }
    singleOf(::FileListUseCaseImpl) { bind<FileListUseCase>() }
    singleOf(::FileManagementUseCaseImpl) { bind<FileManagementUseCase>() }
    singleOf(::ReadPdfUseCaseImpl) { bind<ReadPdfUseCase>() }

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