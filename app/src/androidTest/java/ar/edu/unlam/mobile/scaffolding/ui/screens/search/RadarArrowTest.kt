package ar.edu.unlam.mobile.scaffolding.ui.screens.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

class RadarArrowTest {
    // Regla de Compose para interactuar con la UI
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuando_apunta_correctamente_muestra_descripcion_de_alineado() {
        // ARRANGE (preparar)
        // Renderizamos el componente aislado en la pantalla del test
        composeTestRule.setContent {
            RadarArrow(
                rotation = 0f,
                isPointingCorrectly = true, // Caso de éxito (color verde)
            )
        }

        // ASSERT (verificar)
        // Este texto está en el contentDescription en SearchScreen.kt
        composeTestRule
            .onNodeWithContentDescription("¡Alineado! Camina hacia adelante")
            .assertIsDisplayed()
    }

    @Test
    fun cuando_NO_apunta_correctamente_muestra_descripcion_de_giro() {
        // ARRANGE
        composeTestRule.setContent {
            RadarArrow(
                rotation = 45f,
                isPointingCorrectly = false, // Caso de búsqueda (color rojo)
            )
        }

        // ASSERT
        // Buscamos el texto descriptivo de la flecha roja
        composeTestRule
            .onNodeWithContentDescription("Gira para encontrar la dirección")
            .assertIsDisplayed()
    }
}
