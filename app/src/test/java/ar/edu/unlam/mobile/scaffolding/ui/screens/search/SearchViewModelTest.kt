package ar.edu.unlam.mobile.scaffolding.ui.screens.search

import androidx.lifecycle.SavedStateHandle
import ar.edu.unlam.mobile.scaffolding.domain.model.DeviceOrientation
import ar.edu.unlam.mobile.scaffolding.domain.model.SearchMode
import ar.edu.unlam.mobile.scaffolding.domain.repository.LocationRepository
import ar.edu.unlam.mobile.scaffolding.domain.repository.PetsRepository
import ar.edu.unlam.mobile.scaffolding.domain.repository.RouteRepository
import ar.edu.unlam.mobile.scaffolding.domain.repository.SensorRepository
import ar.edu.unlam.mobile.scaffolding.domain.usecase.CalculateBearingUseCase
import ar.edu.unlam.mobile.scaffolding.rules.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    // Aplicar MainDispatcherRule
    // Esto configura el hilo Main automáticamente antes de cada test
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    // Usamos 'relaxed = true' para que no fallen si llamamos métodos no configurados
    private val locationRepository: LocationRepository = mockk(relaxed = true)
    private val sensorRepository: SensorRepository = mockk(relaxed = true)
    private val routeRepository: RouteRepository = mockk(relaxed = true)
    private val petsRepository: PetsRepository = mockk(relaxed = true)
    private val calculateBearingUseCase: CalculateBearingUseCase = mockk(relaxed = true)

    // Simulamos que recibimos un ID de una mascota desde la navegación
    private val savedStateHandle = SavedStateHandle(mapOf("petId" to "123"))

    // El objeto real
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        // Inicializamos el ViewModel inyectándole los mocks
        viewModel = SearchViewModel(
            locationRepository,
            sensorRepository,
            routeRepository,
            calculateBearingUseCase,
            petsRepository,
            savedStateHandle
        )
    }

    @Test
    fun `al cambiar a modo RADAR, el uiState debe actualizar la orientacion cuando el sensor emite datos`() = runTest {
        // ARRANGE (Preparar)
        // Simulamos un dato que vendría del sensor (ejemplo: 90 grados)
        val orientacionSimulada = DeviceOrientation(azimuth = 90f)

        // Enseñamos al mock: "Cuando te pidan la orientación, devolvé un Flow con este dato"
        every { sensorRepository.getDeviceOrientation() } returns flowOf(orientacionSimulada)

        // ACT (Ejecutar)
        // El usuario toca el botón "Radar"
        viewModel.onSearchModeChanged(SearchMode.RADAR)

        // ASSERT (Verificar)
        // El modo debe haber cambiado
        assertEquals(SearchMode.RADAR, viewModel.uiState.value.searchMode)

        // La orientación en el estado debe coincidir con la del sensor (90 grados)
        // Esto confirma que el ViewModel se suscribió correctamente al Flow
        assertEquals(90f, viewModel.uiState.value.deviceOrientation?.azimuth)
    }
}
