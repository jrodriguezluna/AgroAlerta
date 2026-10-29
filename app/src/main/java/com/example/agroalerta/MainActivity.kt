package com.example.agroalerta

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.agroalerta.data.local.AppDatabase
import com.example.agroalerta.data.local.crearSupervisorPorDefecto
import com.example.agroalerta.ui.AppNavigation
import com.example.agroalerta.ui.theme.AgroAlertaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val dao = AppDatabase.obtenerBaseDeDatos(applicationContext).usuarioDao()
        lifecycleScope.launch { crearSupervisorPorDefecto(dao) } // Evitará la duplicación al hacerlo así
        setContent {
            AgroAlertaTheme {
                AppNavigation(dao)
            }
        }
    }
}
