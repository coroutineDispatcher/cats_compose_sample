package com.coroutinedispatcher.cats

import android.app.Application

class CatsApplication : Application() {

    lateinit var injector: Injector

    override fun onCreate() {
        super.onCreate()
        injector = Injector()
    }
}
