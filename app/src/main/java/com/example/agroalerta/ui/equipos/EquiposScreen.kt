package com.example.agroalerta.ui.equipos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.Equipo
import com.example.agroalerta.data.model.EstadoEquipo
import com.example.agroalerta.data.model.GrupoEquipos
import com.example.agroalerta.data.model.comoEstadoSensor
import com.example.agroalerta.ui.components.AgroCard
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.CajaInformativa
import com.example.agroalerta.ui.components.EncabezadoSeccion
import com.example.agroalerta.ui.components.StatusChip
import com.example.agroalerta.ui.theme.BordeSuave
import com.example.agroalerta.ui.theme.ColoresAgro
import com.example.agroalerta.ui.theme.ColoresEstado

@Composable
fun EquiposScreen(
    rol: Rol,
    iniciales: String,
    grupos: List<GrupoEquipos>,
    onPerfil: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AgroTopBar(titulo = "Equipos", rol = rol, iniciales = iniciales, onPerfil = onPerfil)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
        ) {
            // El MVP solo monitorea: no hay switches para encender/apagar.
            CajaInformativa(
                icono = Icons.Rounded.Visibility,
                titulo = "Monitoreo en modo lectura",
                detalle = "El control remoto no está habilitado."
            )

            grupos.forEach { grupo ->
                EncabezadoSeccion(grupo.titulo)
                AgroCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(13.dp)) {
                    Column {
                        grupo.equipos.forEachIndexed { indice, equipo ->
                            if (indice > 0) HorizontalDivider(color = BordeSuave)
                            FilaEquipo(equipo, rol)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaEquipo(equipo: Equipo, rol: Rol) {
    val (colores, icono) = apariencia(equipo.estado)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(colores.fondo),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = equipo.estado.etiqueta, tint = colores.texto)
        }
        Column(Modifier.weight(1f)) {
            Text(equipo.nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(
                text = if (rol == Rol.SUPERVISOR) "${equipo.ubicacion} · Último cambio: 06:12" else equipo.ubicacion,
                fontSize = 11.sp,
                color = Color(0xFF6C756D)
            )
        }
        StatusChip(
            estado = equipo.estado.comoEstadoSensor(),
            rol = rol,
            // El supervisor ve el estado real del equipo (Encendido, Apagado...); el técnico, solo bien/revisar.
            etiqueta = if (rol == Rol.SUPERVISOR) equipo.estado.etiqueta else null
        )
    }
}

private fun apariencia(estado: EstadoEquipo): Pair<ColoresEstado, ImageVector> = when (estado) {
    EstadoEquipo.ENCENDIDO -> ColoresAgro.normal to Icons.Rounded.PowerSettingsNew
    EstadoEquipo.APAGADO -> ColoresAgro.apagado to Icons.Rounded.PowerSettingsNew
    EstadoEquipo.FALLA -> ColoresAgro.critico to Icons.Rounded.Error
    EstadoEquipo.MANTENCION -> ColoresAgro.mantencion to Icons.Rounded.Build
    EstadoEquipo.SIN_RESPUESTA -> ColoresAgro.sinDatos to Icons.Rounded.CloudOff
}
