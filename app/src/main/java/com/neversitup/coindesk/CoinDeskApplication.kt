package com.neversitup.coindesk

import android.app.Application
import com.neversitup.coindesk.di.AppModule.appModule
import com.neversitup.coindesk.di.AppModule.repoModule
import com.neversitup.coindesk.di.AppModule.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CoinDeskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CoinDeskApplication)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}
