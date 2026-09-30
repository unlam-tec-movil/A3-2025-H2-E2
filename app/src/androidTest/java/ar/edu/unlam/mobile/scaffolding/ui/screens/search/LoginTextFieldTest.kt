package ar.edu.unlam.mobile.scaffolding.ui.screens.search

import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import ar.edu.unlam.mobile.scaffolding.ui.screens.CampoDeTexto
import org.junit.Rule
import org.junit.Test

class LoginTextFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun campoDeTextoOnTextChangeyOnFocusChangeFuncionan() {
        var capturedText: String? = null
        var isFocused: Boolean? = null

        composeTestRule.setContent {
            CampoDeTexto(
                texto = "",
                onTextChange = { capturedText = it },
                onFocusChange = { isFocused = it },
            )
        }

        composeTestRule
            .onNodeWithText("E-mail")
            .assertExists()

        composeTestRule
            .onNode(hasSetTextAction())
            .performClick()

        assert(isFocused == true)

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("hola@test.com")

        assert(capturedText == "hola@test.com")
    }
}
