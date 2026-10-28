package com.example.agroalerta.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.ui.components.AgroCard
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.BotonPeligro

@Composable
fun PerfilScreen(
    nombre: String,
    iniciales: String,
    rol: Rol,
    onAtras: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AgroTopBar(titulo = "Mi perfil", onAtras = onAtras)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCCE7C9)),
                contentAlignment = Alignment.Center
            ) {
                Text(iniciales, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1C6629))
            }
            Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))

            Row(
                modifier = Modifier
                    .padding(top = 9.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0xFFE4F1E1))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(Icons.Rounded.Badge, null, tint = Color(0xFF23692E), modifier = Modifier.size(16.dp))
                Text(
                    if (rol == Rol.SUPERVISOR) "Supervisor" else "Técnico",
                    color = Color(0xFF23692E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(22.dp))
            AgroCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) {
                Column(Modifier.padding(horizontal = 14.dp)) {
                    Dato("PREDIO ASIGNADO", "Agrícola FRUTIS")
                    HorizontalDivider(color = Color(0xFFE7EBE4))
                    Dato("UBICACIÓN", "Buin, Región Metropolitana")
                }
            }

            Spacer(Modifier.height(14.dp))
            BotonPeligro(
                texto = "Cerrar sesión",
                icono = Icons.AutoMirrored.Rounded.Logout,
                onClick = onCerrarSesion
            )
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(etiqueta, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, color = Color(0xFF737C73))
        Text(valor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
