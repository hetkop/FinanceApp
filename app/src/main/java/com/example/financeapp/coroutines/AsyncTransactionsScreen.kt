package com.example.financeapp.coroutines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financeapp.R
import com.example.financeapp.TransactionType

@Composable
@Suppress("FunctionNaming")
internal fun AsyncTransactionsScreen(viewModel: AsyncTransactionsViewModel, onOpenLegacy: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(stringResource(R.string.async_title), style = MaterialTheme.typography.headlineMedium)
        }
        item {
            TextButton(onClick = onOpenLegacy) { Text(stringResource(R.string.async_open_legacy)) }
        }
        item {
            val label = if (state == AsyncTransactionsState.Error) R.string.async_retry else R.string.async_load
            Button(
                onClick = { viewModel.load() },
                enabled = state != AsyncTransactionsState.Loading,
            ) { Text(stringResource(label)) }
        }
        item {
            OutlinedButton(
                onClick = { viewModel.load(shouldFail = true) },
                enabled = state != AsyncTransactionsState.Loading,
            ) { Text(stringResource(R.string.async_simulate_error)) }
        }

        when (val current = state) {
            AsyncTransactionsState.Idle -> item { Text(stringResource(R.string.async_idle)) }

            AsyncTransactionsState.Loading -> {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
                item { Text(stringResource(R.string.transaction_loading)) }
                item {
                    TextButton(onClick = viewModel::cancelLoad) { Text(stringResource(R.string.async_cancel)) }
                }
            }

            is AsyncTransactionsState.Content -> items(current.transactions) { transaction ->
                val label = when (transaction.type) {
                    TransactionType.INCOME -> R.string.transaction_history_income
                    TransactionType.EXPENSE -> R.string.transaction_history_expense
                }
                Text(stringResource(label, transaction.description, transaction.amount))
            }

            AsyncTransactionsState.Error -> item {
                Text(stringResource(R.string.async_error), color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
