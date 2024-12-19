package com.silverst.kpitassignment.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.silverst.kpitassignment.MainViewModel
import com.silverst.kpitassignment.presentation.ui.theme.KPITAssignmentTheme

@Composable
fun AppMainScreen(
    mainViewModel: MainViewModel = MainViewModel(),
    paddingValues: PaddingValues = PaddingValues()
) {
    // Persist inputText across recompositions and configuration changes
    val inputText by mainViewModel.inputText.collectAsState()

    // Observe subsets state from ViewModel
    val subsets by mainViewModel.subsets.collectAsState()

    // States to control visibility of TextFields
    val showSubsets = remember { mutableStateOf(false) }

    val discountNumber by mainViewModel.inputDiscount.collectAsState()

    val totalPrice by mainViewModel.totalPrice.collectAsState()
    val validationError by mainViewModel.validationError.collectAsState()

    mainViewModel.updateProductPrices(listOf(10, 2000))

    Column(modifier = Modifier.fillMaxSize().padding(top = 30.dp, bottom = 50.dp)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(3.dp))
                    .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = inputText,
                        onValueChange = { mainViewModel.updateInputText(it.trim()) },
                        label = { Text("Enter text") },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(modifier = Modifier.weight(1f),
                        onClick = {
                            mainViewModel.findAllSubsets(inputText)
                            showSubsets.value = true
                        }
                    ) {
                        Text("Process String")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Black, RoundedCornerShape(3.dp))
                    .padding(10.dp)
                ) {

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = discountNumber,
                            onValueChange = {
                                mainViewModel.updateInputDiscount(it)
                            },
                            label = { Text("Discount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(modifier = Modifier.weight(1f),
                            onClick = {
                                mainViewModel.calculateTotalPrice(discountNumber)
                                showSubsets.value = false
                            }
                        ) {
                            Text("Total Price")
                        }
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    mainViewModel.clearInputText()
                }) {
                    Text("Clear All Text")
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (showSubsets.value) {
                    TextField(
                        value = subsets
                            .sortedBy { it.length }
                            .joinToString(", "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Output") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }else {
                    TextField(
                        value = totalPrice.toString(),
                        onValueChange = {},
                        readOnly = true,
                        isError = validationError.isNotEmpty(),
                        label = { Text("Output") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .verticalScroll(rememberScrollState())
                    )

                    if (validationError.isNotEmpty()){
                        Text(
                            text = validationError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    KPITAssignmentTheme {
        AppMainScreen()
    }
}