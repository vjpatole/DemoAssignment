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

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun updateInputDiscount(discountValue: String){
        _inputDiscount.value = discountValue
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
    }

    fun getProductsPrices() = listOf(
        10,
        20
    )

    fun calculateTotalPrice(inputStr: String) {
        val discount = inputStr.toInt()
        val prices = getProductsPrices()
        // Check if the list is empty or discount is invalid
        if (prices.isEmpty() || discount < 0 || discount > 100) {
            _totalPrice.value = 0
        } else {
            // Find the most expensive item
            val maxPrice = prices.maxOrNull() ?: 0

            // Calculate the discount amount for the most expensive item
            val discountAmount = maxPrice * discount / 100.0

            // Calculate the total price with the discount applied
            val totalPrice = prices.sum() - discountAmount

            if (totalPrice <= 0){
                _totalPrice.value = 0
            }else{
                // Return the total price rounded down to the nearest integer
                _totalPrice.value = totalPrice.toInt()
            }
        }
    }
}