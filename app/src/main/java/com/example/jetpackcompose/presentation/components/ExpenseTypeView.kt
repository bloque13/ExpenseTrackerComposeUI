package com.example.jetpackcompose.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.domain.model.TransactionType

@Composable
fun ExpenseTypeView(
    isIncomeSelected: Boolean = false,
    onSelectedTransactionTypeChanged: (TransactionType) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ExpenseTypeToggleView(
            transactionType = TransactionType.INCOME,
            isSelected = isIncomeSelected,
            onSelectedCategoryChanged = {
                onSelectedTransactionTypeChanged(TransactionType.INCOME)
            }
        )
        ExpenseTypeToggleView(
            transactionType = TransactionType.EXPENSE,
            isSelected = !isIncomeSelected,
            onSelectedCategoryChanged = {
                onSelectedTransactionTypeChanged(TransactionType.EXPENSE)
            }
        )
    }
}

