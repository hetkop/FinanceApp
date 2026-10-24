package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

internal class TransactionViewModel : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun onEvent(event: MainUiEvent) {
        uiState = reduce(uiState, event)
    }

    private fun reduce(
        state: MainUiState, event: MainUiEvent,
    ): MainUiState = when (event) {
        is MainUiEvent.AmountChanged ->
            if (event.value.all(Char::isDigit))
                state.copy(amount = event.value, error = null)
            else state

        is MainUiEvent.DescriptionChanged ->
            state.copy(description = event.value, error = null)

        is MainUiEvent.TypeChanged ->
            state.copy(type = event.type, error = null)

        MainUiEvent.InvalidTransaction ->
            state.copy(error = TransactionError.INVALID_INPUT)

        MainUiEvent.AmountTooLarge ->
            state.copy(error = TransactionError.AMOUNT_TOO_LARGE)

        is MainUiEvent.AddTransaction -> {
            val tx = event.transaction
            require(tx.amount > 0)
            require(tx.description.isNotBlank())

            state
        }
    }
}



