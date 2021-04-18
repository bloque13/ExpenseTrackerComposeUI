package com.example.jetpackcompose.presentation.components.account

sealed class AccountListEvent {
    object LoadEvent : AccountListEvent()
    object PrePopulateDbEvent : AccountListEvent()
    data class TransactionDeletedEvent(val transactionId: Int) : AccountListEvent()
}