package com.example.financeapp

internal enum class TransactionType { INCOME, EXPENSE }

internal data class Transaction(
    val amount: Int,
    val type: TransactionType,
    val description: String,
)
