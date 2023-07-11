package com.neversitup.coindesk.data

import com.neversitup.coindesk.data.api.Time

data class HistoricalPrice(
    val time: Time,
    val bpi: Map<String, CurrencyPrice>
)