package com.example.financeapp.practice

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.time.measureTime

fun main(args: Array<String>) {
    runBlocking {
        when (args.singleOrNull()) {
            "demo" -> delayDemo()
            "1" -> exercise1()
            "2" -> exercise2()
            "3" -> exercise3()
            "4" -> exercise4()
            "5" -> exercise5()
            "6" -> exercise6()
            "7" -> exercise7()
            "8" -> exercise8()
            else -> println("Передайте demo или номер консольного задания от 1 до 8.")
        }
    }
}

private suspend fun delayDemo(): Unit = coroutineScope {
    launch {
        println("Запрос начался")
        delay(600)
        println("Запрос завершился")
    }
    launch {
        delay(200)
        println("Другая корутина выполнилась во время ожидания")
    }
}

private suspend fun exercise1() {
    println("Начало")
    // Вызовите здесь свою suspend-функцию расчёта баланса.
    println("Готово")
}

private suspend fun exercise2() {
    val income = loadIncome()
    println("Доход: $income ₽")
    println("Запрос запущен")
}

private suspend fun exercise3() {
    val sequentialTime = measureTime {
        val balance = loadIncome() - loadExpenses()
        println("Баланс: $balance ₽")
    }
    println("Последовательно: $sequentialTime")
    // Замерьте второй вариант, где оба запроса уже запущены к моменту первого await().
}

private suspend fun exercise4() {
    println("Поток вызывающего кода: ${Thread.currentThread().name}")
    val balance = readCachedBalance()
    println("Баланс из кеша: $balance ₽")
}

private suspend fun exercise5(): Unit = coroutineScope {
    launch {
        repeat(5) { step ->
            delay(200)
            println("Шаг ${step + 1}")
        }
    }
    // Отмените дочернюю задачу и дождитесь её завершения.
}

private suspend fun exercise6() {
    val items = loadOperations(shouldFail = false)
    println("Загружено операций: ${items.size}")
}

private suspend fun exercise7() {
    val updates = operationUpdates()
    updates.collect { println(it) }
    // Во второй подписке отфильтруйте расходы и преобразуйте их в строки.
}

private suspend fun exercise8() {
    val state = MutableStateFlow("Ожидание")
    println(state.value)
    // Запустите подписчика и обновите состояние при загрузке операций.
}

private suspend fun loadIncome(): Int {
    delay(600)
    return 4_000
}

private suspend fun loadExpenses(): Int {
    delay(900)
    return 900
}

private fun readCachedBalance(): Int {
    println("Поток чтения кеша: ${Thread.currentThread().name}")
    // Имитируем блокирующий API чтения с диска.
    Thread.sleep(400)
    return 3_100
}

private data class Operation(val title: String, val amount: Int)

private val operations = listOf(
    Operation("Подработка", 4_000),
    Operation("Продукты", -650),
    Operation("Кофе", -250),
)

private suspend fun loadOperations(shouldFail: Boolean): List<Operation> {
    delay(600)
    if (shouldFail) throw IOException("Учебная ошибка загрузки")
    return operations
}

private fun operationUpdates() = flow {
    for (operation in operations) {
        emit(operation)
    }
}
