package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import ar.edu.unlam.mobile.scaffolding.ui.theme.ColorTwo
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBar() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = ColorTwo,
            darkIcons = false
        )
    }
}
