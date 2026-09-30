package ar.edu.unlam.mobile.scaffolding.domain.usecase

import ar.edu.unlam.mobile.scaffolding.domain.model.Pet
import ar.edu.unlam.mobile.scaffolding.domain.model.Status
import ar.edu.unlam.mobile.scaffolding.domain.model.User
import ar.edu.unlam.mobile.scaffolding.domain.repository.PetsRepository
import ar.edu.unlam.mobile.scaffolding.domain.repository.UserRepository
import ar.edu.unlam.mobile.scaffolding.rules.MainDispatcherRule
import ar.edu.unlam.mobile.scaffolding.ui.screens.userPosts.MyPetsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPetsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var petsRepo: PetsRepository
    private lateinit var userRepo: UserRepository

    @Before
    fun setup() {
        petsRepo = mockk()
        userRepo = mockk()
    }

    @Test
    fun alInicializarseElViewModelObtieneLasMascotasDelUsuarioYActualizaLaListaDePosts() =
        runTest {
            // Fake data
            val user =
                User(
                    id = "123",
                    postIds = listOf("p1", "p2"),
                    email = "",
                    phone = "",
                )

            val pets =
                listOf(
                    Pet(id = "p1", status = Status.LOST),
                    Pet(id = "p2", status = Status.FOUND),
                )

            coEvery { userRepo.getCurrentUser() } returns user

            coEvery { petsRepo.getPetsByIds(any()) } returns
                flow {
                    emit(pets)
                }

            val vm = MyPetsViewModel(petsRepo, userRepo)

            runCurrent()

            assert(vm.posts.value == pets)
            assert(vm.lostPosts.value.size == 1)
            assert(vm.foundPosts.value.size == 1)
        }
}
