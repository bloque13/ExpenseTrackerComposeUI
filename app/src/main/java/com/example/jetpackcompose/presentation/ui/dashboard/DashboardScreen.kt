package com.example.jetpackcompose.presentation.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.presentation.components.account.AccountListEvent
import com.example.jetpackcompose.presentation.components.account.AccountListView
import com.example.jetpackcompose.presentation.navigation.Screen
import com.example.jetpackcompose.presentation.theme.AppTheme

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun DashboardScreen(
    onNavigateToAddTransactionScreen: (String) -> Unit,
    viewModel: DashboardViewModel,
) {
    val loading = viewModel.loading.value
    val accounts = viewModel.accounts.value
    val transactions = viewModel.transactions.value

    val scaffoldState = rememberScaffoldState()

    AppTheme(
        displayProgressBar = loading,
        scaffoldState = scaffoldState,
        darkTheme = false
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Dashboard",
                            color = Color.Black
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            onNavigateToAddTransactionScreen(
                                Screen.AddTransaction.route
                            )
                        }) {
                            Icon(
                                Icons.Filled.Add,
                                "add transaction",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary,
                    elevation = 12.dp
                )
            },
            content = {
                AccountListView(
                    loading = loading,
                    accounts = accounts,
                    transactions = transactions,
                    onDelete = {
                        viewModel.sendEvent(
                            AccountListEvent.TransactionDeletedEvent(transactionId = it)
                        )
                    },
                    onCancelDelete = {
                        viewModel.sendEvent(AccountListEvent.LoadEvent)
                    }

                )
            }
        )
    }
}