package com.example.jetpackcompose.presentation.ui.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.presentation.components.AmountView
import com.example.jetpackcompose.presentation.components.DropdownListView
import com.example.jetpackcompose.presentation.components.ExpenseTypeView
import com.example.jetpackcompose.presentation.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.NumberFormat
import java.util.*

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel,
    onNavToDashboard: () -> Unit,
) {
    val transactionAmount = viewModel.transactionAmount.value
    val accounts = viewModel.accounts.value
    val selectedCategory = viewModel.selectedCategory.value
    val selectedCategoryMap = viewModel.selectedCategoryMap.value
    val selectedAccount = viewModel.selectedAccount.value
    var isIncomeSelected = viewModel.isIncomeSelected.value
    var confirmTransaction = viewModel.confirmTransaction.value
    val scaffoldState = rememberScaffoldState()
    val transactionSaved = viewModel.transactionSaved.value
    val errorMessage: String = viewModel.errorMessage.value
    val openAddTransactionDialog = remember { mutableStateOf(false) }


    AppTheme(
        displayProgressBar = false,
        scaffoldState = scaffoldState,
        darkTheme = false
    ) {
        if (transactionSaved) {
            onNavToDashboard()
        }

        if (confirmTransaction) {
            openAddTransactionDialog.value = true
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Add Transaction",
                            color = Color.Black
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.validate()
                        }) {
                            Icon(
                                Icons.Filled.Done,
                                "done",
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
                Column {

                    ExpenseTypeView(
                        isIncomeSelected = isIncomeSelected,
                        onSelectedTransactionTypeChanged = {
                            viewModel.selectedTransactionType(it)
                        }
                    )

                    if (errorMessage != "") {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.body2,
                            color = Color.Red,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DropdownListView(
                        title = "Select Account",
                        selectedText = selectedAccount,
                        map = accounts.map { it.id to it.name }.toMap(),
                        onItemSelected = { selectedAccount ->
                            viewModel.selectedAccount.value = selectedAccount
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DropdownListView(
                        title = "Select Category",
                        selectedText = selectedCategory,
                        map = selectedCategoryMap,
                        onItemSelected = { selectedCategory ->
                            viewModel.selectedCategory.value = selectedCategory
                        }
                    )


                    if (openAddTransactionDialog.value) {

                        AlertDialog(
                            onDismissRequest = {
                                // Dismiss the dialog when the user clicks outside the dialog or on the back
                                // button. If you want to disable that functionality, simply use an empty
                                // onCloseRequest.
                                openAddTransactionDialog.value = false
                            },
                            title = {
                                Text(text = "Add Transaction")
                            },
                            text = {

                                var str = NumberFormat.getCurrencyInstance(
                                    Locale(
                                        Locale.current.language,
                                        Locale.current.region
                                    )
                                ).format(transactionAmount)

                                Text("Please confirm you wish to add a $selectedCategory amount of $str to $selectedAccount.")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        openAddTransactionDialog.value = false
                                        viewModel.saveTransaction()
                                    }) {
                                    Text("CONFIRM")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = {
                                        openAddTransactionDialog.value = false
                                        onNavToDashboard()
                                    }) {
                                    Text("CANCEL")
                                }
                            }
                        )
                    }

                    AmountView(onAmountEntered = {
                        viewModel.onAmountEntered(it)
                    })

                }
            }
        )

    }

}