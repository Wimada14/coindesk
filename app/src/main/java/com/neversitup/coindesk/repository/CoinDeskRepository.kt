package com.neversitup.coindesk.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neversitup.coindesk.data.CurrencyPrice
import com.neversitup.coindesk.data.HistoricalPrice
import com.neversitup.coindesk.data.api.CoinDeskApiService
import com.neversitup.coindesk.data.api.CoinDeskResponse
import com.neversitup.coindesk.data.api.Time

class CoinDeskRepository constructor(
    private val apiService: CoinDeskApiService,
    private val sharedPreferences: SharedPreferences
){
    suspend fun getCurrentPrice(): CoinDeskResponse {
        val response = apiService.getCurrentPrice()
        saveHistoricalPriceToLocal(response.time,response.bpi)
        return response
    }
    private fun saveHistoricalPriceToLocal(timeUpdate: Time, bpi: Map<String, CurrencyPrice>) {

        val jsonString = sharedPreferences.getString("historical_prices", null)
        val listType = object : TypeToken<List<HistoricalPrice>>() {}.type
        val historicalPrices: MutableList<HistoricalPrice> = Gson().fromJson(jsonString, listType) ?: mutableListOf()
        val historicalPrice = HistoricalPrice(timeUpdate,bpi)
        if(!historicalPrices.contains(historicalPrice)) {
            historicalPrices.add(historicalPrice)
        }

        val updatedJsonString = Gson().toJson(historicalPrices)
        sharedPreferences.edit {
            putString("historical_prices", updatedJsonString)
        }
    }

    fun getHistoricalPriceFromLocal(): List<HistoricalPrice> {
        val jsonString = sharedPreferences.getString("historical_prices", null)
        val listType = object : TypeToken<List<HistoricalPrice>>() {}.type
        return Gson().fromJson(jsonString, listType) ?: emptyList()
    }
}
