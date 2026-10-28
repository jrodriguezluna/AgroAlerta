package com.example.agroalerta.ui.register

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.local.UsuarioDao
import com.example.agroalerta.data.local.UsuarioEntity
import com.example.agroalerta.utils.hashPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    dao: UsuarioDao,
    onRegisterOk: () -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf(Rol.TECNICO) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta") },
                navigationIcon = { TextButton(onClick = onVolver)  {"Volver"}}
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; error = null },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; error = null },
                label = { Text("Correo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = null },
                label = { Text("Contraseña (mínimo 6 caracteres)") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = confirmar,
                onValueChange = { confirmar = it; error = null },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Rol", style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Rol.entries.forEach { opcion ->
                    RadioButton(selected = rol == opcion, onClick = { rol = opcion })
                    Text(opcion.name.lowercase().replaceFirstChar { it.uppercase() })
                    Spacer(Modifier.width(16.dp))
                }
            }

            AnimatedVisibility(visible = error != null) {
                Text(error ?: "", color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    val emailLimpio = email.trim().lowercase()
                    when {
                        nombre.isBlank() || emailLimpio.isBlank() || password.isBlank() ->
                            error = "Completa todos los campos"
                        !Patterns.EMAIL_ADDRESS.matcher(emailLimpio).matches() ->
                            error = "El correo no es válido"
                        password.length < 6 ->
                            error = "La contraseña debe de tener al menos 6 caracteres"
                        password != confirmar ->
                            error = "Las contraseñas no coinciden"
                        else -> scope.launch {
                            if (dao.existeEmail(emailLimpio) > 0) {
                                error = "Ese correo ya está registrado"
                            } else {
                                dao.registrar(
                                    UsuarioEntity(
                                        nombre = nombre.trim(),
                                        email = emailLimpio,
                                        password = hashPassword(password),
                                        rol = rol.name
                                    )
                                )
                                launch { snackbarHostState.showSnackbar("Cuenta creada. Ya puedes iniciar sesión.") }
                                delay(1200)
                                onRegisterOk()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Registrarme") }
        }

    }
}