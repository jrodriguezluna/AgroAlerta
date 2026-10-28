package com.example.agroalerta.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agroalerta.data.local.DatabaseHelper

@Composable
fun AppNavigation(db: DatabaseHelper) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {  }
        composable("register") {  }
        composable("main") {  }
    }
}