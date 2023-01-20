package com.github.lucascalheiros.common_test.runners

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.github.lucascalheiros.common_test.TestApplication

class InstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, TestApplication::class.java.name, context)
    }
}