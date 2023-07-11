package com.neversitup.coindesk.ui

import com.neversitup.coindesk.InstantTaskExecutorRule
import com.neversitup.coindesk.MainDispatcherRule
import com.neversitup.coindesk.data.api.CoinDeskResponse
import com.neversitup.coindesk.data.api.Time
import com.neversitup.coindesk.repository.CoinDeskRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var coinDeskRepository: CoinDeskRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MainViewModel(coinDeskRepository)
    }

    @Test
    fun testGetCurrentPrice() {
        val expectedResponse = CoinDeskResponse(Time(""), emptyMap())
        coEvery { coinDeskRepository.getCurrentPrice() } returns expectedResponse

        viewModel.getCurrentPrice()

        assertEquals(expectedResponse.bpi, viewModel.currentPrice.value)
    }

    @Test
    fun testStartUpdatingPrice() {
        val expectedResponse = CoinDeskResponse(Time(""), emptyMap())
        coEvery { coinDeskRepository.getCurrentPrice() } returns expectedResponse

        viewModel.startUpdatingPrice()

        assertEquals(expectedResponse.bpi, viewModel.currentPrice.value)
    }

    @Test
    fun testSelectCurrency() {
        val currencyCode = "USD"
        viewModel.selectCurrency(currencyCode)

        assertEquals(currencyCode, viewModel.selectedCurrency.value)
    }

    @Test
    fun testSetConversionAmount() {
        val amount = "100"
        viewModel.setConversionAmount(amount)

        assertEquals(amount, viewModel.conversionAmount.value)
    }
}