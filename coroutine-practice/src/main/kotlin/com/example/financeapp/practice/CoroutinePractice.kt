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
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.yield

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
private suspend fun loadBalance(): Int {
    val income = loadIncome()
    val expenses = loadExpenses()
    return income - expenses
}

private suspend fun exercise1() {
    println("Начало")
    val balance = loadBalance()
    println("Баланс: $balance ₽")
    println("Готово")
}

private suspend fun exercise2() = coroutineScope {
    val job = launch {
        val income = loadIncome()
        println("Доход: $income ₽")
    }
    println("Запрос запущен")
    job.join()
    println("Все задачи завершены")
}

private suspend fun exercise3() = coroutineScope {
    val sequentialStart = System.currentTimeMillis()
    val sequentialBalance = loadIncome() - loadExpenses()
    val sequentialTime = System.currentTimeMillis() - sequentialStart
    println("Последовательный запуск: $sequentialBalance ₽ за $sequentialTime мс")

    val concurrentStart = System.currentTimeMillis()
    val incomeDeferred = async { loadIncome() }
    val expensesDeferred = async { loadExpenses() }
    val concurrentBalance = incomeDeferred.await() - expensesDeferred.await()
    val concurrentTime = System.currentTimeMillis() - concurrentStart
    println("Конкурентный запуск: $concurrentBalance ₽ за $concurrentTime мс")
}



private suspend fun exercise4() {
    println("До чтения: ${Thread.currentThread().name}")
    val balance = withContext(Dispatchers.IO) {
        readCachedBalance()
    }
    println("После возврата: ${Thread.currentThread().name}, баланс: $balance ₽")
}

private suspend fun exercise5() = coroutineScope {
    val job = launch {
        try {
            repeat(5) { step ->
                delay(200)
                println("Шаг ${step + 1}")
            }
        } finally {
            println("Ресурс освобождён")
        }
    }
    delay(450)
    job.cancelAndJoin()
    println("Отмена завершена")
}

private suspend fun exercise6() {
    val fastResult = withTimeoutOrNull(300) {
        loadOperations(shouldFail = false)
    }
    if (fastResult == null) {
        println("300 мс: Время ожидания истекло.")
    } else {
        println("300 мс: Загружено операций: ${fastResult.size}.")
    }
    val normalResult = withTimeoutOrNull(1200) {
        loadOperations(shouldFail = false)
    }
    if (normalResult == null) {
        println("1200 мс: Время ожидания истекло.")
    } else {
        println("1200 мс: Загружено операций: ${normalResult.size}.")
    }
    val operations = try {
        loadOperations(shouldFail = true)
    } catch (e: IOException) {
        println("Учебная ошибка: ${e.message}")
        loadOperations(shouldFail = false)
    }
    println("Повторная загрузка: получено операций: ${operations.size}.")
}


private suspend fun exercise7() {
    println("Первая подписка:")
    operationUpdates().collect { operation ->
        println(operation)
    }

    println("Вторая подписка:")
    operationUpdates()
        .filter { it.amount < 0 }
        .map { "${it.title} — ${-it.amount} ₽" }
        .collect { line ->
            println(line)
        }
}


private suspend fun exercise8() = coroutineScope {
    val state = MutableStateFlow("Ожидание")
    val publicState = state.asStateFlow()

    launch {
        publicState
            .take(3)
            .collect { value ->
                println(value)
            }
    }

    yield()

    state.value = "Загрузка"
    val operations = loadOperations(false)
    state.value = "Загружено: ${operations.size}"
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

private fun operationUpdates(): Flow<Operation> = flow {
    println("Источник запущен")
    for (operation in operations) {
        delay(200)
        emit(operation)
    }
}

