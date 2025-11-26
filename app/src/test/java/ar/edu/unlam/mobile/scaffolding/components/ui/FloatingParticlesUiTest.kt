package ar.edu.unlam.mobile.scaffolding.components.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import ar.edu.unlam.mobile.scaffolding.ui.components.FloatingParticlesBackgroundAnimated
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FloatingParticlesUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun particlesCanvas_rendersSuccessfully() {
        composeTestRule.setContent {
            FloatingParticlesBackgroundAnimated(
                modifier = Modifier,
                particleCount = 10,
            )
        }

        // Verifica que el Canvas con testTag exista
        composeTestRule.onNodeWithTag("particles_canvas").assertExists()
    }
}
