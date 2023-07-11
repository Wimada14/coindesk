package com.neversitup.coindesk.ui
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.neversitup.coindesk.data.HistoricalPrice
import com.neversitup.coindesk.repository.CoinDeskRepository


class HistoryViewModel(
    private val coinDeskRepository: CoinDeskRepository
) : ViewModel() {

    val historicalPrice = mutableStateOf<List<HistoricalPrice>>(emptyList())

    init {
        getHistoricalPrices()
    }

    private fun getHistoricalPrices() {
        val historicalPrices = coinDeskRepository.getHistoricalPriceFromLocal()
        historicalPrice.value = historicalPrices
    }
}