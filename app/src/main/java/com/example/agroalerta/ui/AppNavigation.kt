package com.example.agroalerta.ui

import android.net.Uri
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Popup
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.local.UsuarioDao
import com.example.agroalerta.ui.login.LoginScreen
import com.example.agroalerta.ui.main.MainScreen

object Rutas {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val MAIN = "main/{nombre}/{rol}"
    fun main(nombre: String, rol: String) = "main/${Uri.encode(nombre)}/$rol"
}
@Composable
fun AppNavigation(dao: UsuarioDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it / 3 }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }

    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                dao = dao,
                onLoginOk = { usuario ->
                    navController.navigate(Rutas.main(usuario.nombre, usuario.rol)) {
                        popUpTo(Rutas.LOGIN) {inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTER)}
            )
        }

        composable(Rutas.REGISTER) {

        }

        composable(
            route = Rutas.MAIN,
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val rol = Rol.valueOf(backStackEntry.arguments?.getString("rol") ?: Rol.TECNICO.name)
            MainScreen(
                nombre = nombre,
                rol = rol,
                onCerrarSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )

        }
    }
}