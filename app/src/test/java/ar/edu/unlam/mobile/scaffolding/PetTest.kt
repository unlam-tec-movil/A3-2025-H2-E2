package ar.edu.unlam.mobile.scaffolding

import android.net.Uri
import ar.edu.unlam.mobile.scaffolding.domain.model.DeviceOrientation
import ar.edu.unlam.mobile.scaffolding.domain.model.Pet
import ar.edu.unlam.mobile.scaffolding.domain.model.Pin

import ar.edu.unlam.mobile.scaffolding.domain.model.SearchMode
import ar.edu.unlam.mobile.scaffolding.domain.model.Status
import ar.edu.unlam.mobile.scaffolding.domain.model.Type
import ar.edu.unlam.mobile.scaffolding.domain.repository.PetsRepository
import ar.edu.unlam.mobile.scaffolding.domain.repository.UserRepository
import ar.edu.unlam.mobile.scaffolding.ui.screens.posts.PostViewModel
import com.google.firebase.storage.FirebaseStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test


class PetTest : Any() {
    @get:Rule

    //private var petRepository = mockk<PetsRepository>()
    private lateinit var viewModel : PostViewModel
    private var userRepository = mockk<UserRepository>()
    private var petsRepository = mockk<PetsRepository>()
    private var firebaseStorage = mockk<FirebaseStorage>()

@Before
fun setUp() {
    // Inicializamos el ViewModel

        viewModel = PostViewModel(
        userRepository,
        petsRepository,
        firebaseStorage,)
}


    @Test
    fun `que No Se PuedaCrear Una Publicacion Sin Ubicacion`() = runTest {

        // given

        val exception = Exception("No se puede crear una publicación sin ubicación")

        every {  viewModel.savePet(any(), any(), any())} //returns exception

       // val pin: Pin? = null   // <- sin ubicación*/
        val pet = Pet(
            id = "3123124",
            name = "Richard",
            type = Type.DOG
        )

        // when
       // viewModel.savePet( onSuccessMessage = {} )

        // then
        // 1) nunca se llama al repo si no hay ubicación

        verify(exactly = 0) {

        }
        val stringPa = petsRepository.savePet(pet)

        print(stringPa)
        // 2) el estado de UI muestra error (ajustá al nombre real de tu estado)
        //    val state = viewModel.uiState.value
        //   assertEquals("", state.errorMessage)
    }
}
