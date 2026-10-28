package com.example.agroalerta

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.agroalerta.data.local.AppDatabase
import com.example.agroalerta.ui.AppNavigation
import com.example.agroalerta.ui.theme.AgroAlertaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        val dao = AppDatabase.obtenerBaseDeDatos(applicationContext).usuarioDao()
        setContent {
            AgroAlertaTheme {
                AppNavigation(dao)
            }
        }
    }
}
