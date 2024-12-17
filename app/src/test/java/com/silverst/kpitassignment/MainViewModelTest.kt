package com.silverst.kpitassignment

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = MainViewModel()
        viewModel.resetSubsets()
    }


    @After
    fun tearDown() {
        // Reset the Main dispatcher to the original dispatcher
        Dispatchers.resetMain()
    }

    //updateInputText
    @Test
    fun test_updateInputText_updates_inputText() = runTest {
        val newText = "Hello World"
        viewModel.updateInputText(newText)
        viewModel.inputText.test {
            assertEquals(newText, awaitItem())
        }
    }

    //updateInputDiscount
    @Test
    fun test_updateInputDiscount_updates_inputDiscount() = runTest {
        val discount = "15"
        viewModel.updateInputDiscount(discount)
        viewModel.inputDiscount.test {
            assertEquals(discount, awaitItem())
        }
    }

    //findAllSubsets
    @Test
    fun test_findAllSubsets_generates_subsets_correctly() = runTest {
        val input = "abc"
        val expectedSubsets = listOf("a", "ab", "abc", "b", "bc", "c")
        viewModel.findAllSubsets(input)
        advanceUntilIdle()
        viewModel.subsets.test {
            assertEquals(expectedSubsets, awaitItem())
        }
    }

    @Test
    fun test_findAllSubsets_with_empty_input_generates_no_subsets() = runTest {
        val input = ""
        viewModel.findAllSubsets(input)
        viewModel.subsets.test {
            assertEquals(emptyList<String>(), awaitItem())
        }
    }

    //clearInputText
    @Test
    fun test_clearInputText_resets_inputText_inputDiscount_and_subsets() = runTest {
        viewModel.updateInputText("Test Text")
        viewModel.updateInputDiscount("20")
        viewModel.findAllSubsets("abc")

        viewModel.clearInputText()

        advanceUntilIdle()

        viewModel.inputText.test {
            assertEquals("", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.inputDiscount.test {
            assertEquals("", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.subsets.test {
            assertEquals(emptyList<String>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    //getProductsPrices
    @Test
    fun test_getProductsPrices_returns_correct_prices() {
        val expectedPrices = listOf(10, 20)
        assertEquals(expectedPrices, viewModel.getProductsPrices())
    }

    //calculateTotalPrice
    @Test
    fun test_calculateTotalPrice_calculates_correctly() = runTest {
        val discount = "10" // 10% discount
        viewModel.calculateTotalPrice(discount)
        viewModel.totalPrice.test {
            assertEquals(28, awaitItem()) // 10 + 20 = 30; discount on 20 is 2; 30 - 2 = 28
        }
    }

    @Test
    fun test_calculateTotalPrice_with_invalid_discount() = runTest {
        viewModel.calculateTotalPrice("110") //more than 100

        advanceUntilIdle()

        viewModel.totalPrice.test {
            assertEquals(0, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.calculateTotalPrice("-10") // less than 0

        advanceUntilIdle()

        viewModel.totalPrice.test {
            assertEquals(0, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
