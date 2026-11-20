package com.example.financeapp.legacy2

import androidx.room.TypeConverter
import com.example.financeapp.TransactionType

internal class TransactionTypeConverter {
    @TypeConverter
    fun fromStoredValue(value: String): TransactionType = TransactionType.valueOf(value)

    @TypeConverter
    fun toStoredValue(type: TransactionType): String = type.name
}
