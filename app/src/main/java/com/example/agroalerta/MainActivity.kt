package com.example.agroalerta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.agroalerta.data.local.AppDatabase
import com.example.agroalerta.ui.AppNavigation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dao = AppDatabase.obtenerBaseDeDatos(applicationContext).usuarioDao()
        setContent {
            AppNavigation(dao)
        }
    }
}

