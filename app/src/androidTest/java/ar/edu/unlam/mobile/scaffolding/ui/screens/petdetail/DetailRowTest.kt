package ar.edu.unlam.mobile.scaffolding.ui.screens.petdetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class DetailRowTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailRow_showsLabelAndValue_correctly() {
        // GIVEN
        val testLabel = "Visto última vez en:"
        val testValue = "Dirección 1000"

        // WHEN
        composeTestRule.setContent {
            DetailRow(
                label = testLabel,
                value = testValue,
            )
        }

        // THEN
        composeTestRule.onNodeWithText(testLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(testValue).assertIsDisplayed()
    }
}
