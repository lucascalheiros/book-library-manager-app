package com.github.lucascalheiros.booklibrarymanager.di

import android.app.Application
import androidx.room.Room
import com.github.lucascalheiros.booklibrarymanager.data.storage.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@ComponentScan("com.github.lucascalheiros.booklibrarymanager")
class AppModule

val db = module {
    fun provideDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "AppDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { provideDataBase(androidApplication()) }
}
