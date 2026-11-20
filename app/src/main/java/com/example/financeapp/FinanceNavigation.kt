package com.example.financeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financeapp.coroutines.AsyncTransactionsScreen
import com.example.financeapp.coroutines.AsyncTransactionsViewModel
import com.example.financeapp.legacy2.LEGACY2_ROUTE
import com.example.financeapp.legacy2.legacy2Graph

private const val ASYNC_TRANSACTIONS_ROUTE = "async-transactions"

@Composable
@Suppress("FunctionNaming")
internal fun FinanceNavigation() {
    val navController = rememberNavController()
    var showLegacyBalance by rememberSaveable { mutableStateOf(true) }

    NavHost(navController = navController, startDestination = ASYNC_TRANSACTIONS_ROUTE) {
        composable(ASYNC_TRANSACTIONS_ROUTE) {
            val viewModel: AsyncTransactionsViewModel = viewModel()
            AsyncTransactionsScreen(
                viewModel = viewModel,
                onOpenLegacy = {
                    viewModel.cancelLoad()
                    navController.navigate(LEGACY2_ROUTE) { launchSingleTop = true }
                },
            )
        }
        legacy2Graph(
            navController = navController,
            showBalance = { showLegacyBalance },
            onShowBalanceChange = { showLegacyBalance = it },
        )
    }
}
