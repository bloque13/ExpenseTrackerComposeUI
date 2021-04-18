package com.example.jetpackcompose.presentation.navigation

sealed class Screen(
    val route: String,
) {
    object Dashboard: Screen("dashboard")

    object AddTransaction: Screen("add-transaction")
}
