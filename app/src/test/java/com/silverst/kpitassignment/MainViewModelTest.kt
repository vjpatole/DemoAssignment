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
import kotlin.math.exp

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
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
    fun test_updateInputText_updates_inputText() = runTest(testDispatcher) {
        val newText = "Hello World"
        viewModel.updateInputText(newText)
        viewModel.inputText.test {
            assertEquals(newText, awaitItem())
        }
    }

    //updateInputDiscount
    @Test
    fun test_updateInputDiscount_updates_inputDiscount() = runTest(testDispatcher) {
        val discount = "15"
        viewModel.updateInputDiscount(discount)
        viewModel.inputDiscount.test {
            assertEquals(discount, awaitItem())
        }
    }

    //findAllSubsets
    @Test
    fun test_findAllSubsets_generates_subsets_correctly() = runTest(testDispatcher) {
        val input = "abc"
        val expectedSubsets = listOf("a", "ab", "abc", "b", "bc", "c")
        viewModel.findAllSubsets(input)

        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.subsets.test {
            assertEquals(expectedSubsets, awaitItem())
        }
    }

    @Test
    fun test_findAllSubsets_with_empty_input_generates_no_subsets() = runTest(testDispatcher) {
        val input = ""
        viewModel.findAllSubsets(input)
        viewModel.subsets.test {
            assertEquals(emptyList<String>(), awaitItem())
        }
    }

    //clearInputText
    @Test
    fun test_clearInputText_resets_inputText_inputDiscount_and_subsets() = runTest(testDispatcher) {
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
    fun test_getProductsPrices_empty_discount_value() = runTest(testDispatcher){
        val discount = ""
        val expectedPrices = listOf(10, 20)
        viewModel.updateProductPrices(expectedPrices)


        viewModel.calculateTotalPrice(discount)

        assertEquals("Enter Discount between 1-100", viewModel.validationError.value)
    }

    @Test
    fun test_getProductsPrices_invalid_discount_negative() = runTest(testDispatcher){
        val discount = "-10"
        val expectedPrices = listOf(10, 20)
        viewModel.updateProductPrices(expectedPrices)

        viewModel.calculateTotalPrice(discount)

        assertEquals("Discount must be between 0 and 100.", viewModel.validationError.value)
    }

    @Test
    fun test_getProductsPrices_invalid_discount_positive() = runTest(testDispatcher){
        val discount = "110"
        val expectedPrices = listOf(10, 20)
        viewModel.updateProductPrices(expectedPrices)

        viewModel.calculateTotalPrice(discount)

        assertEquals("Discount must be between 0 and 100.", viewModel.validationError.value)
    }

    @Test
    fun test_getProductsPrices_empty_products() = runTest(testDispatcher){
        val discount = "10"
        val expectedPrices = emptyList<Int>()
        viewModel.updateProductPrices(expectedPrices)

        viewModel.calculateTotalPrice(discount)

        assertEquals("Number of products must be between 1 and 99.", viewModel.validationError.value)
    }

    @Test
    fun test_getProductsPrices_invalid_product_price() = runTest(testDispatcher){
        val discount = "10"
        val expectedPrices = listOf(100, 200000)
        viewModel.updateProductPrices(expectedPrices)

        viewModel.calculateTotalPrice(discount)

        assertEquals("Price of each product must be between 1 and 99,999.", viewModel.validationError.value)
    }

    @Test
    fun test_getProductsPrices_many_number_of_products() = runTest(testDispatcher){
        val discount = "10"
        val expectedPrices = List(100){ it + 1}
        viewModel.updateProductPrices(expectedPrices)

        viewModel.calculateTotalPrice(discount)

        assertEquals("Number of products must be between 1 and 99.", viewModel.validationError.value)
    }

    @Test
    fun test_getProductsPrices_returns_correct_prices() = runTest(testDispatcher) {
        val expectedPrices = listOf(10, 20)

        viewModel.updateProductPrices(expectedPrices)

        assertEquals(expectedPrices, viewModel.productPrices.value)
    }

    //calculateTotalPrice
    @Test
    fun test_calculateTotalPrice_calculates_correctly() = runTest(testDispatcher) {
        val prices = listOf(10, 20)
        viewModel.updateProductPrices(prices)
        val discount = "10" // 10% discount
        viewModel.calculateTotalPrice(discount)
        viewModel.totalPrice.test {
            assertEquals(28, awaitItem()) // 10 + 20 = 30; discount on 20 is 2; 30 - 2 = 28
        }
    }
}
