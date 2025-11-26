package ar.edu.unlam.mobile.scaffolding.components.ui

import androidx.compose.ui.graphics.Color
import ar.edu.unlam.mobile.scaffolding.ui.components.Particle
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class FloatingParticlesLogicTest {
    @Test
    fun particle_generation_hasValidRanges() {
        val random = Random(1234) // Semilla fija para reproducibilidad

        val particles =
            List(10) { i ->
                Particle(
                    id = i,
                    radius = random.nextInt(10, 22).toFloat(),
                    color = Color.Magenta,
                    baseX = random.nextFloat(),
                    baseY = random.nextFloat(),
                    speedX = random.nextInt(4000, 8000),
                    speedY = random.nextInt(5000, 10000),
                    pulseSpeed = random.nextInt(2500, 5000),
                )
            }

        // Verificamos que todos los radios estén en el rango esperado
        assertTrue(particles.all { it.radius in 10f..22f })

        // Verificamos que las posiciones estén dentro de [0f, 1f]
        assertTrue(particles.all { it.baseX in 0f..1f && it.baseY in 0f..1f })

        // Verificamos que las velocidades estén en los rangos definidos
        assertTrue(particles.all { it.speedX in 4000..8000 })
        assertTrue(particles.all { it.speedY in 5000..10000 })
        assertTrue(particles.all { it.pulseSpeed in 2500..5000 })
    }
}
