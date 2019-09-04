package com.se.stories

import android.app.Application
import com.facebook.stetho.Stetho
import com.se.stories.module.ApiModule
import com.se.stories.module.ConnectivityModule
import com.se.stories.module.DbModule
import com.se.stories.repository.IStoriesRepository
import com.se.stories.repository.StoriesRepository
import com.se.stories.viewmodel.StoriesActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class StoriesApplication : Application() {

    private var moduleList = module {
        single { ApiModule() }
        single { DbModule(this@StoriesApplication) }
        single { ConnectivityModule(this@StoriesApplication) }

        single<IStoriesRepository> { StoriesRepository(get(), get())}

        viewModel { StoriesActivityViewModel(get(), get()) }
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
        //initStetho()

        startKoin {
            androidLogger()
            androidContext(this@StoriesApplication)
            modules(moduleList)
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    // Only For Debug
    /*private fun initStetho() {
        Stetho.initializeWithDefaults(this)
    }*/
}
