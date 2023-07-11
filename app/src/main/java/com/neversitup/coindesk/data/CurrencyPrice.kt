package com.neversitup.coindesk.data

data class CurrencyPrice(
    val code: String,
    val symbol: String,
    val rate: String,
    val description: String,
    val rateFloat: Double
)