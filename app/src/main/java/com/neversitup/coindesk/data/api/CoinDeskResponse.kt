package com.neversitup.coindesk.data.api

import com.neversitup.coindesk.data.CurrencyPrice

data class CoinDeskResponse(
    val time: Time,
    val bpi: Map<String, CurrencyPrice>
)

data class Time(
    val updated: String
)