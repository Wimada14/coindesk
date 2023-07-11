package com.neversitup.coindesk.ui
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neversitup.coindesk.data.CurrencyPrice
import com.neversitup.coindesk.repository.CoinDeskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainViewModel(
    private val coinDeskRepository: CoinDeskRepository
) : ViewModel() {

    private val _currentPrice = mutableStateOf(emptyMap<String, CurrencyPrice>())
    val currentPrice: State<Map<String, CurrencyPrice>> = _currentPrice

    private val _selectedCurrency = mutableStateOf("")
    val selectedCurrency: State<String> = _selectedCurrency

    private val _conversionAmount = mutableStateOf("")
    val conversionAmount: State<String> = _conversionAmount

    init {
        getCurrentPrice()
        startUpdatingPrice()
    }

    fun getCurrentPrice() {
        viewModelScope.launch {
            try {
                val response = coinDeskRepository.getCurrentPrice()
                _currentPrice.value = response.bpi
            } catch (_: Exception) {
            }
        }
    }

    fun startUpdatingPrice() {
         viewModelScope.launch {
             while (true) {
                 try {
                     getCurrentPrice()
                     delay(60000) // 1 minute delay
                 } catch (_: Exception) {
                 }
             }
        }
    }

    fun selectCurrency(currencyCode: String) {
        _selectedCurrency.value = currencyCode
    }

    fun setConversionAmount(amount: String) {
        _conversionAmount.value = amount
    }
}
