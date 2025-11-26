package ar.edu.unlam.mobile.scaffolding.ui.screens.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import ar.edu.unlam.mobile.scaffolding.domain.model.Pet
import ar.edu.unlam.mobile.scaffolding.domain.model.Status
import ar.edu.unlam.mobile.scaffolding.domain.model.Type
import ar.edu.unlam.mobile.scaffolding.domain.repository.LocationRepository
import ar.edu.unlam.mobile.scaffolding.domain.repository.PetsRepository
import io.mockk.every
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class MapTest {

    // regla para ejecutar tareas en el mismo hilo
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Dispatcher de prueba para Coroutines
    val mainDispatcherRule = StandardTestDispatcher()

    private val mockLocationRepository: LocationRepository = mock()
    private val mockPetsRepository: PetsRepository = mock()
    private lateinit var viewModel: MapViewModel

@Before
fun setup(){
    Dispatchers.setMain(mainDispatcherRule)

    viewModel = MapViewModel(mockLocationRepository, mockPetsRepository)
}
    @After
    fun tearDown() {
        // resetea el Main dispatcher despues de cada test
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPets debe cargar la lista de mascotas y cambiar el estado de carga`()
    = runTest(mainDispatcherRule) {

        // GIVEN:

        val mockPetList =listOf(
            Pet(
                id = "1",
                name = "EL rrope",
                latitude = 0.0,
                longitude = 0.0,
                imageUrl = "",
                type = Type.DOG,
                status = Status.FOUND
            )
        )

        //se le da comportamiento al mock

        every {(mockPetsRepository.getAllPets())} returns (flowOf(mockPetList))

        // WHEN:

        // loadPets() se llama en init {}

        // THEN: los estados dse reflejan en el resultado de la carga

        assertEquals(1, viewModel.pets.value.size)
        assertFalse(viewModel.isLoadingPets.value)
    }
}
