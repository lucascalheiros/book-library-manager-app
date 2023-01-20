package com.github.lucascalheiros.booklibrarymanager.utils

import com.github.lucascalheiros.data_authentication.authenticationModules
import com.github.lucascalheiros.data_drive_file.driveFileModules
import com.github.lucascalheiros.feature_account.accountModule
import com.github.lucascalheiros.feature_home.presentation.homeModule
import com.github.lucascalheiros.feature_login.loginModule
import com.github.lucascalheiros.feature_pdfreader.pdfReaderModule
import org.koin.core.module.Module

object ProdAppModules {

    private val featureModules = listOf(homeModule, pdfReaderModule, loginModule, accountModule)

    val appModulesList: List<Module> = listOf(driveFileModules, authenticationModules, featureModules).flatten()
}