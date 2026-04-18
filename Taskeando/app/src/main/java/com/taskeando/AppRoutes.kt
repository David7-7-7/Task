package com.taskeando

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ScaleFactor
import com.taskeando.ui.screens.SplashScreen
import com.taskeando.ui.screens.TareaUI
import com.taskeando.ui.screens.*

sealed class Screen {
    object Splash : Screen()

    object Login : Screen()
    object Tareas : Screen()
    object Calendario : Screen()
    object CrearNota : Screen()
    data class Detalle(val tarea: TareaUI) : Screen()
}

@Composable
fun AppRoutes() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

    when(val screen = currentScreen ){

        is Screen.Splash -> SplashScreen (
            onEmpezar = {currentScreen = Screen.Login }
        )

        is Screen.Login -> LoginScreen(
            onLogin = { currentScreen = Screen.Tareas }
        )

        is Screen.Tareas -> MisTareasScreen(
            onNuevaTarea = {currentScreen = Screen.CrearNota},
            onVerCalendario = {currentScreen = Screen.Calendario},
            onVerDetalle = { tarea -> currentScreen = Screen.Detalle(tarea )}
        )

        is Screen.Calendario -> CalendarioScreen(
            onVerTareas = {currentScreen = Screen.Tareas},
            onNuevaTarea = {currentScreen = Screen.Tareas}
        )

        is Screen.CrearNota -> CrearNotaScreen(
            onBack = {currentScreen = Screen.Tareas},
            onGuardar = {currentScreen = Screen.Tareas}
        )

        is Screen.Detalle -> DetalleTareaScreen(
            tarea = screen.tarea,
            onBack = {currentScreen = Screen.Tareas},
            onEditar = {currentScreen = Screen.Tareas},
            onVerTareas = {currentScreen = Screen.Tareas },
            onVerCalendario = {currentScreen = Screen.Calendario},
            onNuevaTarea = {currentScreen = Screen.CrearNota}
        )
    }
}