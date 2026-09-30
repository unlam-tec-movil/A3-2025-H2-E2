package ar.edu.unlam.mobile.scaffolding.rules

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Regla de JUnit para reemplazar Dispatchers.Main con un TestDispatcher
 * en tests unitarios de ViewModels.
 * Es necesario porque:
 * - Los ViewModels usan viewModelScope.launch que internamente usa Dispatchers.Main
 * - Dispatchers.Main solo existe en Android (UI thread)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    /**
     * Se ejecuta ANTES de cada test.
     * Reemplaza Dispatchers.Main con el TestDispatcher.
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Se ejecuta DESPUÉS de cada test.
     * Restaura Dispatchers.Main a su estado original.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
