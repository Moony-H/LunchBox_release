package com.kimleehanjang.lunchbox.refactoring

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LunchApplication: Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: LunchApplication
        fun getApplicationContext(): Context {
            return instance.applicationContext
        }
    }
}