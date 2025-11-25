package ar.edu.unlam.mobile.scaffolding.ui.screens.petdetail

import ar.edu.unlam.mobile.scaffolding.domain.model.Pet
import ar.edu.unlam.mobile.scaffolding.domain.repository.PetsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PetDetailViewModelTest {
    private lateinit var viewModel: PetDetailViewModel
    private val repository: PetsRepository = mockk()
    private val firebaseAuth: FirebaseAuth = mockk()
    private val firebaseUser: FirebaseUser = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "test_user_id"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `loadPet updates pet state correctly when repository returns data`() =
        runTest {
            // GIVEN
            val petId = "123"
            val expectedPet =
                Pet(
                    id = "123",
                    name = "Zeus",
                    ownerId = "owner_1",
                )

            coEvery { repository.getPetById(petId) } returns flowOf(expectedPet)

            viewModel = PetDetailViewModel(repository)

            // WHEN
            viewModel.loadPet(petId)

            // THEN
            assertEquals(expectedPet, viewModel.pet.value)
        }
}
