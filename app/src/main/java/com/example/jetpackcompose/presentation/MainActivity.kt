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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.example.jetpackcompose.presentation.ui.dashboard.DashboardScreen
import com.example.jetpackcompose.presentation.ui.dashboard.DashboardViewModel
import com.example.jetpackcompose.presentation.ui.transaction.AddTransactionScreen
import com.example.jetpackcompose.presentation.ui.transaction.AddTransactionViewModel

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
@ExperimentalFoundationApi
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    var currentScreen: String = ""
    var navController: NavHostController? = null
    lateinit var dashBoardiewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            navController?.let { navController ->

                navController.addOnDestinationChangedListener(this)

                NavHost(navController = navController, startDestination = Screen.Dashboard.route) {

                    composable(route = Screen.Dashboard.route) { navBackStackEntry ->
                        dashBoardiewModel = hiltNavGraphViewModel<DashboardViewModel>(navBackStackEntry)
                        DashboardScreen(
                            viewModel = dashBoardiewModel,
                            onNavigateToAddTransactionScreen = { route ->
                                navController.navigate(route)
                            }
                        )
                    }

                    composable(route = Screen.AddTransaction.route) {
                        AddTransactionScreen(
                            viewModel = hiltNavGraphViewModel<AddTransactionViewModel>(it),
                            onNavToDashboard = {
                                navController.navigate(Screen.Dashboard.route)
                            }
                        )
                    }
                }
            }

        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val destination = destination.arguments["android-support-nav:controller:route"]?.defaultValue.toString()
        if(currentScreen == "add-transaction" && destination == "dashboard")
        {
            dashBoardiewModel.reload()
        }
        currentScreen = destination

    }
}
