package com.example.jetpackcompose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import com.example.jetpackcompose.domain.model.Transaction
import com.example.jetpackcompose.domain.model.TransactionType
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

fun LocalDate.isYesterday(): Boolean = this.isEqual(LocalDate.now().minusDays(1L))

fun LocalDate.isToday(): Boolean = this.isEqual(LocalDate.now())

fun Date.toDateLabel(): String {
    val currentDate = Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDate()

    var dateLabel = this.formatToViewDateDefaults()
    val timeLabel = this.formatToHHmm()

    if (currentDate.isToday()) {
        dateLabel = "Today"
    } else if (currentDate.isYesterday()) {
        dateLabel = "Yesterday"
    }

    return "$dateLabel $timeLabel"

}

fun Transaction.transactionFormat(): String {
    val currencyInstance = NumberFormat.getCurrencyInstance(
        java.util.Locale(
            Locale.current.language,
            Locale.current.region
        )
    )
    currencyInstance.roundingMode = RoundingMode.CEILING

    var amount = currencyInstance.format(this.amount)

    return if (this.type == TransactionType.INCOME) "$amount" else "- $amount"
}

fun Transaction.transactionColorise(): Color {
    return if (this.type == TransactionType.INCOME) Color.Green else Color.Red
}

fun String.digitsOnly(): String {
    val regex = Regex("[^0-9]")

    return regex.replace(this, "")
}

fun String.isInteger() = this.toIntOrNull().let { true }