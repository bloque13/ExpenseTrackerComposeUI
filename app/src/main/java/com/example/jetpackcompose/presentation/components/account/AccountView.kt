package com.example.jetpackcompose.presentation.components.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.domain.model.Account
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.math.RoundingMode
import java.text.NumberFormat

@ExperimentalCoroutinesApi
@Composable
fun AccountView(
    account: Account,
) {
    var balance = account.transactions.sumByDouble { it.amount }

    val currencyInstance = NumberFormat.getCurrencyInstance(
        java.util.Locale(
            Locale.current.language,
            Locale.current.region
        )
    )
    currencyInstance.roundingMode = RoundingMode.CEILING

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(top = 20.dp, bottom = 14.dp, start = 12.dp, end = 22.dp)
    ) {

        Text(
            text = account.name,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentWidth(Alignment.Start),
            style = MaterialTheme.typography.h3
        )
        Text(
            text = currencyInstance.format(balance),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.caption
        )
    }

}