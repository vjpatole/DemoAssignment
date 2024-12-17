package com.silverst.kpitassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.silverst.kpitassignment.presentation.composables.AppMainScreen
import com.silverst.kpitassignment.presentation.ui.theme.KPITAssignmentTheme

class MainActivity : ComponentActivity() {
    //lateinit var mainViewModelFactory: MainViewModelFactory
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mainViewModelFactory = MainViewModelFactory()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            KPITAssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppMainScreen(mainViewModel = viewModel, paddingValues = innerPadding)
                }
            }
        }
    }
}

