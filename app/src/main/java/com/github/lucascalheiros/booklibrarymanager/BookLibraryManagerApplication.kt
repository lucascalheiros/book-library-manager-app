package com.github.lucascalheiros.booklibrarymanager

import android.app.Application
import com.github.lucascalheiros.booklibrarymanager.di.AppModule
import com.github.lucascalheiros.data_authentication.authenticationModules
import com.github.lucascalheiros.data_drive_file.driveFileModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module

class BookLibraryManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BookLibraryManagerApplication)
            modules(AppModule().module)
            modules(driveFileModules)
            modules(authenticationModules)
        }
    }

}