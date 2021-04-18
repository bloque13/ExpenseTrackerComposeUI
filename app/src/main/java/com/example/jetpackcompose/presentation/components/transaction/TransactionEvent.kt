package com.example.jetpackcompose.presentation.components.transaction

sealed class TransactionEvent {

    object LoadEvent : TransactionEvent()
    object SaveTransactionEvent : TransactionEvent()
    object TransactionConfirmEvent : TransactionEvent()
    object TransactionSavedEvent : TransactionEvent()

}