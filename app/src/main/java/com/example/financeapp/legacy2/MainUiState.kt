package com.example.financeapp.legacy2

import com.example.financeapp.Transaction
import com.example.financeapp.TransactionType

internal data class MainUiState(
    val amount: String = "",
    val description: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val balance: Int = 0,
    val transactionCount: Int = 0,
    val transactions: List<Transaction> = emptyList(),
    val error: TransactionError? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDatabaseReady: Boolean = false,
)

internal enum class TransactionError { INVALID_INPUT, AMOUNT_TOO_LARGE, DATABASE_ERROR }

internal sealed interface MainUiEvent {
    data class AmountChanged(val value: String) : MainUiEvent
    data class DescriptionChanged(val value: String) : MainUiEvent
    data class TypeChanged(val type: TransactionType) : MainUiEvent
    data class AddTransaction(val transaction: Transaction) : MainUiEvent
    data object InvalidTransaction : MainUiEvent
    data object AmountTooLarge : MainUiEvent
}
