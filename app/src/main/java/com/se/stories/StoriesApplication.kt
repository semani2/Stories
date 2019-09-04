package com.se.stories

import android.app.Application
import timber.log.Timber

class StoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
