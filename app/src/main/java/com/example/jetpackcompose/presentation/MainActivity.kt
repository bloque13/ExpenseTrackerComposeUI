package com.example.jetpackcompose.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.*
import com.example.jetpackcompose.presentation.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.example.jetpackcompose.presentation.ui.dashboard.DashboardScreen
import com.example.jetpackcompose.presentation.ui.dashboard.DashboardViewModel
import com.example.jetpackcompose.presentation.ui.transaction.AddTransactionScreen
import com.example.jetpackcompose.presentation.ui.transaction.AddTransactionViewModel

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
@ExperimentalFoundationApi
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

                composable(route = Screen.Dashboard.route) { navBackStackEntry ->
                    DashboardScreen(
                        viewModel = hiltNavGraphViewModel<DashboardViewModel>(navBackStackEntry),
                        onNavigateToAddTransactionScreen = { route ->
                            navController.popBackStack()
                            navController.navigate(route)
                        }
                    )
                }

                composable(route = Screen.AddTransaction.route) {
                    AddTransactionScreen(
                        viewModel = hiltNavGraphViewModel<AddTransactionViewModel>(it),
                        onNavToDashboard = {
                            navController.popBackStack()
                            navController.navigate(Screen.Dashboard.route)
                        }
                    )
                }

            }
        }
    }

}
