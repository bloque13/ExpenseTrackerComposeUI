package com.example.jetpackcompose.presentation.components.account

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.domain.model.Account
import com.example.jetpackcompose.domain.model.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun AccountListView(
    loading: Boolean,
    accounts: List<Account>,
    transactions: List<Transaction>,
    onDelete: (Int) -> Unit,
    onCancelDelete: () -> Unit,
) {

    val grouped = transactions.groupBy { it.accountId }

    if (loading && accounts.isEmpty()) {
        Text("Loading...")
    } else {

        LazyColumn {
            grouped.forEach { (accountId, transactions) ->
                stickyHeader {
                    AccountView(account = accounts.first { it.id == accountId })
                }
                items(transactions) { transaction ->
                    TransactionView(
                        transaction = transaction,
                        onDelete = onDelete,
                        onCancelDelete = onCancelDelete
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}

