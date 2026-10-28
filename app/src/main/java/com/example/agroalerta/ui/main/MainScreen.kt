package com.example.agroalerta.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.EstadoGestion
import com.example.agroalerta.ui.alertas.AlertaDetalleScreen
import com.example.agroalerta.ui.alertas.AlertasScreen
import com.example.agroalerta.ui.cuarteles.CuartelDetalleScreen
import com.example.agroalerta.ui.cuarteles.CuartelesScreen
import com.example.agroalerta.ui.equipos.EquiposScreen
import com.example.agroalerta.ui.home.HomeScreen
import com.example.agroalerta.ui.perfil.PerfilScreen
import com.example.agroalerta.ui.theme.FondoApp
import com.example.agroalerta.ui.theme.FondoNav
import com.example.agroalerta.ui.theme.RojoCritico
import com.example.agroalerta.ui.theme.VerdeContenedor
import com.example.agroalerta.ui.theme.VerdeTexto

/** Rutas internas de la zona con barra de navegación inferior. */
object RutasMain {
    const val HOME = "home"
    const val CUARTELES = "cuarteles"
    const val CUARTEL = "cuartel/{id}"
    const val EQUIPOS = "equipos"
    const val ALERTAS = "alertas"
    const val ALERTA = "alerta/{id}"
    const val PERFIL = "perfil"

    fun cuartel(id: Int) = "cuartel/$id"
    fun alerta(id: Int) = "alerta/$id"
}

private data class DestinoTab(
    val ruta: String,
    /** Prefijo para saber que estamos en esta pestaña (incluye sus pantallas de detalle). */
    val prefijo: String,
    val etiqueta: String,
    val icono: ImageVector
)

private val Tabs = listOf(
    DestinoTab(RutasMain.HOME, "home", "Inicio", Icons.Rounded.Home),
    DestinoTab(RutasMain.CUARTELES, "cuartel", "Cuarteles", Icons.Rounded.GridView),
    DestinoTab(RutasMain.EQUIPOS, "equipos", "Equipos", Icons.Rounded.Settings),
    DestinoTab(RutasMain.ALERTAS, "alerta", "Alertas", Icons.Rounded.Notifications)
)

private fun iniciales(nombre: String): String =
    nombre.trim().split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }

@Composable
fun MainScreen(
    nombre: String,
    rol: Rol,
    onCerrarSesion: () -> Unit,
    vm: MonitoreoViewModel = viewModel()
) {
    val nav = rememberNavController()
    val entradaActual by nav.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    val alertas by vm.alertas.collectAsState()
    val desactualizado by vm.datosDesactualizados.collectAsState()
    val alertasActivas = alertas.count { it.estado != EstadoGestion.RESUELTA }
    val iniciales = remember(nombre) { iniciales(nombre) }

    fun irATab(ruta: String) {
        nav.navigate(ruta) {
            popUpTo(RutasMain.HOME) { inclusive = false }
            launchSingleTop = true
        }
    }

    Scaffold(
        containerColor = FondoApp,
        bottomBar = {
            // El perfil se abre desde la barra superior y no muestra la barra inferior.
            if (rutaActual != RutasMain.PERFIL) {
                BarraInferior(rutaActual, alertasActivas, onSeleccionar = ::irATab)
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = RutasMain.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(RutasMain.HOME) {
                HomeScreen(
                    rol = rol,
                    iniciales = iniciales,
                    cuarteles = vm.cuarteles,
                    sistemas = vm.sistemas,
                    alertasActivas = alertasActivas,
                    desactualizado = desactualizado,
                    onPerfil = { nav.navigate(RutasMain.PERFIL) },
                    onIrAlertas = { irATab(RutasMain.ALERTAS) },
                    onAbrirCuartel = { nav.navigate(RutasMain.cuartel(it)) },
                    onAlternarDesactualizado = vm::alternarDatosDesactualizados
                )
            }

            composable(RutasMain.CUARTELES) {
                CuartelesScreen(
                    rol = rol,
                    iniciales = iniciales,
                    cuarteles = vm.cuarteles,
                    desactualizado = desactualizado,
                    onPerfil = { nav.navigate(RutasMain.PERFIL) },
                    onAbrirCuartel = { nav.navigate(RutasMain.cuartel(it)) },
                    onAlternarDesactualizado = vm::alternarDatosDesactualizados
                )
            }

            composable(
                route = RutasMain.CUARTEL,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                val cuartel = vm.cuarteles.firstOrNull { it.id == id }
                if (cuartel != null) {
                    CuartelDetalleScreen(
                        cuartel = cuartel,
                        alertas = alertas.filter { it.cuartelId == id && it.estado != EstadoGestion.RESUELTA },
                        rol = rol,
                        desactualizado = desactualizado,
                        onAtras = { nav.popBackStack() },
                        onAbrirAlerta = { nav.navigate(RutasMain.alerta(it)) }
                    )
                }
            }

            composable(RutasMain.EQUIPOS) {
                EquiposScreen(
                    rol = rol,
                    iniciales = iniciales,
                    grupos = vm.gruposEquipos,
                    onPerfil = { nav.navigate(RutasMain.PERFIL) }
                )
            }

            composable(RutasMain.ALERTAS) {
                AlertasScreen(
                    rol = rol,
                    iniciales = iniciales,
                    alertas = alertas,
                    desactualizado = desactualizado,
                    onPerfil = { nav.navigate(RutasMain.PERFIL) },
                    onAbrirAlerta = { nav.navigate(RutasMain.alerta(it)) },
                    onAlternarDesactualizado = vm::alternarDatosDesactualizados
                )
            }

            composable(
                route = RutasMain.ALERTA,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("id") ?: 0
                val alerta = alertas.firstOrNull { it.id == id }
                if (alerta != null) {
                    AlertaDetalleScreen(
                        alerta = alerta,
                        rol = rol,
                        onAtras = { nav.popBackStack() },
                        onMarcarRevisada = { vm.marcarRevisada(id, nombre) }
                    )
                }
            }

            composable(RutasMain.PERFIL) {
                PerfilScreen(
                    nombre = nombre,
                    iniciales = iniciales,
                    rol = rol,
                    onAtras = { nav.popBackStack() },
                    onCerrarSesion = onCerrarSesion
                )
            }
        }
    }
}

@Composable
private fun BarraInferior(
    rutaActual: String?,
    alertasActivas: Int,
    onSeleccionar: (String) -> Unit
) {
    NavigationBar(containerColor = FondoNav) {
        Tabs.forEach { tab ->
            val seleccionada = rutaActual?.startsWith(tab.prefijo) == true
            val badge = if (tab.ruta == RutasMain.ALERTAS) alertasActivas else 0

            NavigationBarItem(
                selected = seleccionada,
                onClick = { onSeleccionar(tab.ruta) },
                icon = {
                    if (badge > 0) {
                        BadgedBox(badge = {
                            Badge(containerColor = RojoCritico, contentColor = Color.White) { Text(badge.toString()) }
                        }) {
                            Icon(tab.icono, contentDescription = tab.etiqueta)
                        }
                    } else {
                        Icon(tab.icono, contentDescription = tab.etiqueta)
                    }
                },
                label = { Text(tab.etiqueta, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = VerdeContenedor,
                    selectedIconColor = VerdeTexto,
                    selectedTextColor = VerdeTexto,
                    unselectedIconColor = Color(0xFF586257),
                    unselectedTextColor = Color(0xFF586257)
                )
            )
        }
    }
}
