package com.example.financeapp

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AsyncTransactionsPracticeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun exercise9() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.async_load)).performClick()
        // Дождитесь результата и проверьте, что операция видна.
    }

    @Test
    fun exercise10() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.async_simulate_error)).performClick()
        // Дождитесь ошибки, нажмите «Повторить» и проверьте появление операции.
    }
}
