package com.silverst.kpitassignment

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.silverst.kpitassignment.presentation.composables.AppMainScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    //verify input text
    @Test
    fun testInputTextUpdates(){
        val viewModel = MainViewModel()

        composeTestRule.setContent {
            AppMainScreen(mainViewModel = viewModel)
        }

        val testInput = "Hello World"
        composeTestRule.onNodeWithText("Enter text").performTextInput(testInput)

        composeTestRule.waitForIdle()

        assertEquals(testInput.trim(), viewModel.inputText.value)
    }

    @Test
    fun testSubsetGenerationFromString(){
        val viewModel = MainViewModel()
        composeTestRule.setContent {
            AppMainScreen(mainViewModel = viewModel)
        }

        val testInput = "abc"
        val expectedResult = listOf("a", "ab", "abc", "b", "bc", "c")

        composeTestRule.onNodeWithText("Enter text").performTextInput(testInput)
        composeTestRule.onNodeWithText("Process String").performClick()

        composeTestRule.waitForIdle()

        assertEquals(expectedResult, viewModel.subsets.value)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.silverst.kpitassignment", appContext.packageName)
    }
}