package ar.edu.unlam.mobile.scaffolding.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Verificar el azimut (bearing) entre dos coordenadas.
 * El azimut es el ángulo medido desde el norte en sentido horario (0-360°).
 */

class CalculateBearingUseCaseTest {
    // Instancia de la clase a probar
    private lateinit var calculateBearingUseCase: CalculateBearingUseCase

    // @Before se ejecuta antes de cada @Test
    @Before
    fun setUp() {
        calculateBearingUseCase = CalculateBearingUseCase()
    }

    /**
     * TEST 1
     * Dado dos puntos geográficos, verificamos que se retorne un ángulo válido
     * dentro del círculo de 360 grados.
     */
    @Test
    fun `el bearing debe estar entre 0 y 360 grados`() {
        // ARRANGE (preparar datos)
        val moronLat = -34.6506
        val moronLon = -58.6226
        val unlamLat = -34.6736
        val unlamLon = -58.5623

        // ACT (ejecutar)
        val bearing =
            calculateBearingUseCase.invoke(
                moronLat,
                moronLon,
                unlamLat,
                unlamLon,
            )

        // ASSERT (verificar resultados, que sea válido el grado)
        assertTrue("El rumbo debe ser mayor o igual a 0", bearing >= 0f)
        assertTrue("El rumbo debe ser menor o igual a 360", bearing <= 360f)
    }

    /**
     * TEST 2
     * Verificamos comportamiento cuando la distancia entre puntos es cero.
     */
    @Test
    fun `si el origen y destino son la misma coordenada, el bearing debe ser 0`() {
        // ARRANGE (preparar datos)
        val unlamLat = -34.6736
        val unlamLon = -58.5623

        // ACT (ejecutar la lógica pasando el mismo punto como origen y destino
        val bearing =
            calculateBearingUseCase.invoke(
                unlamLat,
                unlamLon,
                unlamLat,
                unlamLon,
            )

        // ASSERT (verificar resultados)
        assertEquals("El bearing debe ser 0", 0f, bearing, 0.01f)
    }

    /**
     * TEST 3
     * Si me muevo puramente hacia el norte
     * el bearing debe ser 0 grados.
     */
    @Test
    fun `viajar hacia el norte geografico debe dar bearing 0`() {
        // ARRANGE
        val origenLat = 0.0
        val origenLon = 0.0
        val destinoLat = 1.0 // Un grado hacia el norte
        val destinoLon = 0.0 // Se mantiene misma longitud

        // ACT
        val bearing =
            calculateBearingUseCase.invoke(
                origenLat,
                origenLon,
                destinoLat,
                destinoLon,
            )

        // ASSERT
        assertEquals("Ir al norte debe dar 0 grados", 0f, bearing, 0.01f)
    }

    /**
     * TEST 4
     * Si de Morón a Unlam fui al sureste, volve de Unlam a Morón
     * debe ser dirección noroeste (entre 270° y 360°).
     */
    @Test
    fun `la vuelta de UNLaM a Moron debe ser direccion Noroeste`() {
        // ARRANGE - Se invierte origen y destino con respecto al test 1
        val unlamLat = -34.6736
        val unlamLon = -58.5623
        val moronLat = -34.6506
        val moronLon = -58.6226

        // ACT
        val bearing =
            calculateBearingUseCase.invoke(
                unlamLat,
                unlamLon,
                moronLat,
                moronLon,
            )

        // ASSERT - Noroeste está en el cuarto cuadrante (270° a 360°)
        val esNoroeste = bearing > 270f && bearing < 360f

        assertTrue("La vuelta debería apuntar al Noroeste", esNoroeste)
    }

    /**
     * TEST 5
     * Verificar los 4 puntos cardinales desde el centro (0,0)
     */
    @Test
    fun `debe calcular correctamente los 4 puntos cardinales`() {
        // ARRANGE
        val centro = 0.0

        // ACT & ASSERT - Norte
        val bearingNorte = calculateBearingUseCase(centro, centro, 1.0, 0.0)
        assertEquals("Norte debe ser 0", 0f, bearingNorte, 0.01f)

        // ACT & ASSERT - Este
        val bearingEste = calculateBearingUseCase(centro, centro, 0.0, 1.0)
        assertEquals("Este debe ser 90", 90f, bearingEste, 0.01f)

        // ACT & ASSERT - Sur
        val bearingSur = calculateBearingUseCase(centro, centro, -1.0, 0.0)
        assertEquals("Sur debe ser 180", 180f, bearingSur, 0.01f)

        // ACT & ASSERT - Oeste
        val bearingOeste = calculateBearingUseCase(centro, centro, 0.0, -1.0)
        assertEquals("Oeste debe ser 270", 270f, bearingOeste, 0.01f)
    }
}
