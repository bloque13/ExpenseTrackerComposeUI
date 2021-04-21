package com.example.jetpackcompose.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import java.text.NumberFormat

@Composable
fun AmountView(
    onAmountEntered: (Double) -> Unit
) {

    val buttonPadding = 4.dp
    val buttonDimension = 60.dp

    val amount: MutableState<String> = remember {
        mutableStateOf("0")
    }

    val amountLabel: MutableState<String> = remember {
        mutableStateOf("0.00")
    }

    val currencyLabel = NumberFormat.getCurrencyInstance(
        java.util.Locale(
            Locale.current.language,
            Locale.current.region
        )
    ).format(0)[0]

    fun formatAmountLabel() {
        try {
            val result = amount.value.toDouble() / 100
            val dec = DecimalFormat("#,###.##")
            val str = dec.format(result)
            amountLabel.value = str
            onAmountEntered(result)
        } catch (e: Exception) {

        }
    }

    fun numberSelected(text: Int) {
        if (amount.value == "0") {
            amount.value = text.toString()
        } else {
            amount.value += text.toString()
        }
        formatAmountLabel()
    }

    fun deleteLast() {
        if (amount.value.length == 1) {
            amount.value = "0"
        } else {
            amount.value = amount.value.dropLast(1)
        }
        formatAmountLabel()
    }

    Column {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = "$currencyLabel"
            )
            Text(
                text = "${amountLabel.value}"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 1..3) {
                Button(
                    modifier = Modifier
                        .height(buttonDimension)
                        .width(buttonDimension)
                        .padding(buttonPadding),
                    onClick = {
                        numberSelected(i)
                    }) {
                    Text(
                        text = i.toString()
                    )
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                modifier = Modifier
                    .height(buttonDimension)
                    .width(buttonDimension)
                    .padding(buttonPadding),
                onClick = { }
            ) {
                Text(
                    text = ""
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 4..6) {
                Button(
                    modifier = Modifier
                        .height(buttonDimension)
                        .width(buttonDimension)
                        .padding(buttonPadding),
                    onClick = {
                        numberSelected(i)
                    }) {
                    Text(
                        text = i.toString()
                    )
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                modifier = Modifier
                    .height(buttonDimension)
                    .width(buttonDimension)
                    .padding(buttonPadding),
                onClick = { }
            ) {
                Text(
                    text = ""
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 7..9) {
                Button(
                    modifier = Modifier
                        .height(buttonDimension)
                        .width(buttonDimension)
                        .padding(buttonPadding),
                    onClick = {
                        numberSelected(i)
                    }) {
                    Text(
                        text = i.toString()
                    )
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                modifier = Modifier
                    .height(buttonDimension)
                    .width(buttonDimension)
                    .padding(buttonPadding),
                onClick = {
                    deleteLast()
                }) {
                Text(
                    text = "<"
                )
            }

        }

    }

}

