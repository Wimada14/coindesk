package com.neversitup.coindesk.data.api
import retrofit2.http.GET

interface CoinDeskApiService {
    @GET("v1/bpi/currentprice.json")
    suspend fun getCurrentPrice(): CoinDeskResponse
}