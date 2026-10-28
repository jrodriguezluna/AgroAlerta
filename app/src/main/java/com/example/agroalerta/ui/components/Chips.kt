package com.example.agroalerta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.FiberNew
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.EstadoGestion
import com.example.agroalerta.data.model.EstadoSensor
import com.example.agroalerta.data.model.Severidad
import com.example.agroalerta.ui.theme.ColoresAgro
import com.example.agroalerta.ui.theme.ColoresEstado

/** Etiqueta genérica (ícono opcional + texto) sobre un fondo de color. */
@Composable
fun Etiqueta(
    texto: String,
    colores: ColoresEstado,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    compacta: Boolean = false
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(if (compacta) 7.dp else 9.dp))
            .background(colores.fondo)
            .padding(horizontal = if (compacta) 7.dp else 8.dp, vertical = if (compacta) 4.dp else 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colores.texto,
                modifier = Modifier.size(if (compacta) 12.dp else 15.dp)
            )
        }
        Text(
            text = texto,
            color = colores.texto,
            fontSize = if (compacta) 11.sp else 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

/** Chip de estado de sensor/equipo. Cambia según el rol (supervisor ve 4 estados, técnico 3). */
@Composable
fun StatusChip(
    estado: EstadoSensor,
    rol: Rol,
    modifier: Modifier = Modifier,
    etiqueta: String? = null,
    compacto: Boolean = false
) {
    val visual = estado.visual(rol)
    Etiqueta(
        texto = etiqueta ?: visual.texto,
        colores = visual.colores,
        icono = visual.icono,
        modifier = modifier,
        compacta = compacto
    )
}

@Composable
fun ChipSeveridad(severidad: Severidad, modifier: Modifier = Modifier) {
    Etiqueta(severidad.etiqueta, severidad.colores(), modifier, compacta = true)
}

@Composable
fun ChipGestion(estado: EstadoGestion, modifier: Modifier = Modifier) {
    val (colores, icono) = when (estado) {
        EstadoGestion.NUEVA -> ColoresAgro.nueva to Icons.Rounded.FiberNew
        EstadoGestion.REVISADA -> ColoresAgro.revisada to Icons.Rounded.TaskAlt
        EstadoGestion.RESUELTA -> ColoresAgro.resuelta to Icons.Rounded.CheckCircle
    }
    Etiqueta(estado.etiqueta, colores, modifier, icono, compacta = true)
}
