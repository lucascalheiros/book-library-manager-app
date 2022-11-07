package com.github.lucascalheiros.booklibrarymanager

import android.app.Application
import com.github.lucascalheiros.data_authentication.authenticationModules
import com.github.lucascalheiros.data_drive_file.driveFileModules
import com.github.lucascalheiros.feature_account.accountModule
import com.github.lucascalheiros.feature_home.presentation.homeModule
import com.github.lucascalheiros.feature_login.loginModule
import com.github.lucascalheiros.feature_pdfreader.pdfReaderModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BookLibraryManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BookLibraryManagerApplication)
            modules(driveFileModules)
            modules(authenticationModules)
            modules(homeModule, pdfReaderModule, loginModule, accountModule)
        }
    }

}