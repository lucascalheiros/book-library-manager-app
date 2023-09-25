package com.github.lucascalheiros.data_drive_file

import com.github.lucascalheiros.data_drive_file.data.repository.DriveFileRepositoryImpl
import com.github.lucascalheiros.data_drive_file.data.repository.FileCacheRepositoryImpl
import com.github.lucascalheiros.data_drive_file.data.repository.LocalFileRepositoryImpl
import com.github.lucascalheiros.data_drive_file.domain.repository.DriveFileRepository
import com.github.lucascalheiros.data_drive_file.domain.repository.FileCacheRepository
import com.github.lucascalheiros.data_drive_file.domain.repository.LocalFileRepository
import com.github.lucascalheiros.data_drive_file.domain.usecase.FetchFilesUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileManagementUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.FileSyncUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.ReadPdfUseCase
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.FetchFilesUseCaseImpl
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.FileManagementUseCaseImpl
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.SyncFilesUseCaseImpl
import com.github.lucascalheiros.data_drive_file.domain.usecase.impl.ReadPdfUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val driveFileModuleData = module {
    singleOf(::DriveFileRepositoryImpl) { bind<DriveFileRepository>() }
    singleOf(::FileCacheRepositoryImpl) { bind<FileCacheRepository>() }
    singleOf(::LocalFileRepositoryImpl) { bind<LocalFileRepository>() }
}

val driveFileModuleUseCase = module {
    singleOf(::FetchFilesUseCaseImpl) { bind<FetchFilesUseCase>() }
    singleOf(::FileManagementUseCaseImpl) { bind<FileManagementUseCase>() }
    singleOf(::ReadPdfUseCaseImpl) { bind<ReadPdfUseCase>() }
    singleOf(::SyncFilesUseCaseImpl) { bind<FileSyncUseCase>()  }
}

val driveFileModules = listOf(
    driveFileModuleData,
    driveFileModuleUseCase
)