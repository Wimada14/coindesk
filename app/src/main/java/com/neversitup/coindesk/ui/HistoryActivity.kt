package com.neversitup.coindesk.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neversitup.coindesk.data.HistoricalPrice
import com.neversitup.coindesk.ui.theme.TestTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : ComponentActivity() {
    private val historyViewModel : HistoryViewModel by viewModel()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    "History",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                    content = { innerPadding ->
                        Column(
                            Modifier.padding(innerPadding) ,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            HistoryContent(historyViewModel)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HistoryContent(
    viewModel: HistoryViewModel
) {
    val historicalPrice = viewModel.historicalPrice.value

    val context = LocalContext.current
    val intent = (context as HistoryActivity).intent
    val currency = intent.getStringExtra("currency")

    Column(modifier = Modifier.padding(16.dp)) {
        CurrencyRatesHistory(
            currency = currency.toString(),
            currencyRates = historicalPrice
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyRatesHistory(
    currency: String,
    currencyRates: List<HistoricalPrice>
) {
    LazyColumn {
        item {
            Text("Currency Name : $currency")
        }
        item {
            currencyRates.forEach { (times, data) ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Row(modifier = Modifier.padding(20.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = times.updated,
                                style = TextStyle(
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Text(
                            text = data[currency]!!.rate,
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
