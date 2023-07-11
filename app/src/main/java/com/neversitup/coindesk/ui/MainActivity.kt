package com.neversitup.coindesk.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neversitup.coindesk.R
import com.neversitup.coindesk.data.CurrencyPrice
import com.neversitup.coindesk.ui.theme.TestTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    private val mainViewModel : MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                CoinDeskContent(mainViewModel)
            }
        }
    }
}

@Composable
fun CoinDeskContent(viewModel: MainViewModel) {
    val currentPrice = viewModel.currentPrice.value
    val selectedCurrency = viewModel.selectedCurrency.value
    val conversionAmount = viewModel.conversionAmount.value

    Column(
        modifier = Modifier.padding(16.dp)) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin_desk),
                contentDescription = ""
            )
        }

        CurrencyRates(
            currencyRates = currentPrice
        )

        Spacer(modifier = Modifier.padding(4.dp))

        CurrencyPicker(
            currencies = currentPrice.keys.toList(),
            onCurrencySelected = { currencyCode ->
                viewModel.selectCurrency(currencyCode)
            }
        )

        Spacer(modifier = Modifier.padding(4.dp))
        ConversionInput(
            conversionAmount = conversionAmount,
            onAmountChanged = { amount ->
                viewModel.setConversionAmount(amount)
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        ConversionResult(
            conversionAmount = conversionAmount,
            selectedCurrency = selectedCurrency,
            currencyRates = currentPrice
        )
    }
}
@Composable
fun CurrencyPicker(
    currencies: List<String>,
    onCurrencySelected: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedCurrency = remember { mutableStateOf("") }
    Column {
        Text("Currency Convert")
        Box(modifier = Modifier.padding(8.dp)) {
            OutlinedButton(
                onClick = { expanded.value = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedCurrency.value.ifEmpty { "Select Currency" }
                )
            }

            if (expanded.value) {
                AlertDialog(
                    onDismissRequest = { expanded.value = false },
                    title = { Text(text = "Select Currency") },
                    confirmButton = {
                        currencies.forEach { currency ->
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .clickable {
                                        selectedCurrency.value = currency
                                        onCurrencySelected(currency)
                                        expanded.value = false
                                    }
                            ) {
                                Text(
                                    text = currency,
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                                Divider(color = Color.Gray)
                            }
                        }
                    }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyRates(
    currencyRates: Map<String, CurrencyPrice>,
) {
    val mContext = LocalContext.current
    LazyColumn {
        item {
            Text("Currency Rates")
        }
        item {
            currencyRates.forEach { (currency, data) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Row(modifier = Modifier.padding(20.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${data.description} ($currency)",
                                style = TextStyle(
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = data.rate,
                                style = TextStyle(
                                    fontSize = 16.sp
                                )
                            )
                        }

                        Button(onClick = {
                            mContext.startActivity(Intent(mContext, HistoryActivity::class.java).putExtra("currency",currency))
                        }) {
                            Text(
                                text = "History",
                                style = TextStyle(
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversionInput(
    conversionAmount: String,
    onAmountChanged: (String) -> Unit
) {
    val localFocusManager = LocalFocusManager.current
    TextField(
        value = conversionAmount,
        onValueChange = onAmountChanged,
        label = { Text("Enter Conversion Amount") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            localFocusManager.clearFocus()
        }),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ConversionResult(
    conversionAmount: String,
    selectedCurrency: String,
    currencyRates: Map<String, CurrencyPrice>
) {

    val selectedRate = currencyRates[selectedCurrency]
    val convertedAmount = remember(conversionAmount, selectedRate) {
        val rate = selectedRate?.rate?.replace(",","")?.toDoubleOrNull()
        val amount = conversionAmount.toDoubleOrNull()
        rate?.let { r ->
            amount?.let { a -> (a/r) }
        }
    }

    Column {
        Text("Conversion Result")

        Spacer(modifier = Modifier.padding(4.dp))

        selectedRate?.let {
            Text("Selected Currency: ${it.code}")
            Text("Selected Rate: ${it.rate}")
            Text("Conversion Amount: $conversionAmount ${it.code}")
            convertedAmount?.let { amount ->
                val formattedNumber = String.format("%.8f", amount)
                Text("Converted Amount: $formattedNumber BTC")
            }
        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    TestTheme {
//        CoinDeskContent()
//    }
//}


