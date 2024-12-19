package com.silverst.kpitassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private var findSubsetsJob: Job? = null

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _subsets = MutableStateFlow<List<String>>(emptyList())
    val subsets: StateFlow<List<String>> = _subsets.asStateFlow()

    private val _productPrices  = MutableStateFlow<List<Int>>(emptyList())
    val productPrices: StateFlow<List<Int>> = _productPrices.asStateFlow()

    private val _inputDiscount = MutableStateFlow("")
    val inputDiscount: StateFlow<String> = _inputDiscount.asStateFlow()

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice :StateFlow<Int> = _totalPrice

    private val _validationError = MutableStateFlow("")
    val validationError: StateFlow<String> get() = _validationError

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun updateInputDiscount(discountValue: String){
        _inputDiscount.value = discountValue
    }

    fun updateProductPrices(productPriceList: List<Int>){
        _productPrices.value = productPriceList
    }

    fun findAllSubsets(inputStr: String) {
        findSubsetsJob?.cancel() // Cancel previous job if running
        findSubsetsJob = viewModelScope.launch {
            if (inputStr.isEmpty()) {
                _subsets.value = emptyList()
            } else {
                val generatedSubsets = mutableListOf<String>()
                for (start in inputStr.indices) {
                    for (end in (start + 1)..inputStr.length) {
                        generatedSubsets.add(inputStr.substring(start, end))
                    }
                }
                _subsets.value = generatedSubsets
            }
        }
    }

    // Test-only method to reset state
    fun resetSubsets() {
        _subsets.value = emptyList()
    }

    fun clearInputText() {
        findSubsetsJob?.cancel()
        _inputText.value = ""
        _inputDiscount.value = ""
        _subsets.value = emptyList()
        _productPrices.value = emptyList()
    }

    fun calculateTotalPrice(inputStr: String) {

        if (inputStr.isEmpty()){
            _validationError.value = "Enter Discount between 1-100"
            return
        }

        val discount = inputStr.toInt()
        val prices = productPrices.value


        // Validate discount constraint
        if (discount < 0 || discount > 100) {
            _validationError.value = "Discount must be between 0 and 100."
            return
        }

        // Validate price constraints
        if (prices.isEmpty() || prices.size >= 100) {
            _validationError.value = "Number of products must be between 1 and 99."
            return
        }

        if (prices.any { it <= 0 || it >= 100_000 }) {
            _validationError.value = "Price of each product must be between 1 and 99,999."
            return
        }

        _validationError.value = ""

        // Find the most expensive product
        val maxPrice = prices.maxOrNull() ?: 0

        // Calculate the discount amount for the most expensive item
        val discountAmount = (maxPrice * discount) / 100.0

        // Calculate the total price after applying the discount
        val total = prices.sum() - discountAmount

        // Return the total rounded down to the nearest integer
        _totalPrice.value = total.toInt()
    }
}