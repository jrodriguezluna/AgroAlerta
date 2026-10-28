package com.example.agroalerta.ui.cuarteles

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.Cuartel
import com.example.agroalerta.ui.components.AgroCard
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.StaleBanner
import com.example.agroalerta.ui.components.StatusChip
import com.example.agroalerta.ui.components.visual

@Composable
fun CuartelesScreen(
    rol: Rol,
    iniciales: String,
    cuarteles: List<Cuartel>,
    desactualizado: Boolean,
    onPerfil: () -> Unit,
    onAbrirCuartel: (Int) -> Unit,
    onAlternarDesactualizado: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AgroTopBar(titulo = "Cuarteles", rol = rol, iniciales = iniciales, onPerfil = onPerfil)
        if (desactualizado) StaleBanner(compacto = true)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
        ) {
            Text("Estado por sector", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "${cuarteles.size} cuarteles monitoreados",
                fontSize = 13.sp,
                color = Color(0xFF687268),
                modifier = Modifier.padding(top = 3.dp, bottom = 15.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                cuarteles.forEach { cuartel ->
                    TarjetaCuartel(cuartel, rol, onClick = { onAbrirCuartel(cuartel.id) })
                }
            }

            TextButton(onClick = onAlternarDesactualizado, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(
                    if (desactualizado) Icons.Rounded.CloudDone else Icons.Rounded.CloudOff,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(if (desactualizado) "Ocultar aviso" else "Simular datos desactualizados", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun TarjetaCuartel(cuartel: Cuartel, rol: Rol, onClick: () -> Unit) {
    val visual = cuartel.estado.visual(rol)
    AgroCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), onClick = onClick) {
        Row(
            modifier = Modifier.padding(13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 42.dp, height = 46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(visual.colores.fondo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    cuartel.id.toString(),
                    color = visual.colores.texto,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(cuartel.nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(Icons.Rounded.Sensors, null, tint = Color(0xFF697269), modifier = Modifier.size(14.dp))
                    Text(cuartel.estacion, fontSize = 12.sp, color = Color(0xFF697269))
                }
                // El supervisor ve el dato concreto; el técnico no.
                if (rol == Rol.SUPERVISOR && cuartel.resumenSupervisor != null) {
                    Text(
                        cuartel.resumenSupervisor,
                        fontSize = 11.sp,
                        color = Color(0xFF765300),
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            StatusChip(cuartel.estado, rol)
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = Color(0xFF697269))
        }
    }
}
