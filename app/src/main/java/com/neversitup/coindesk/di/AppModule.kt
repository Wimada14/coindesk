package com.neversitup.coindesk.di

import android.preference.PreferenceManager
import com.neversitup.coindesk.data.api.CoinDeskApiService
import com.neversitup.coindesk.repository.CoinDeskRepository
import com.neversitup.coindesk.ui.HistoryViewModel
import com.neversitup.coindesk.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    val appModule = module {
        single<CoinDeskApiService> {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.coindesk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(CoinDeskApiService::class.java)
        }
        single {
            PreferenceManager.getDefaultSharedPreferences(androidContext())
        }
    }
    val repoModule = module {
        single {
            CoinDeskRepository(get(),get())
        }
    }
    val viewModelModule = module {
        viewModel {
            MainViewModel(get())
        }
        viewModel {
            HistoryViewModel(get())
        }
    }
}