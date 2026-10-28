package com.example.agroalerta.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import com.example.agroalerta.data.model.Alerta
import com.example.agroalerta.data.model.EstadoGestion

/** Cuadro con el ícono del tipo de alerta, coloreado según severidad. */
@Composable
fun IconoAlerta(alerta: Alerta, tamano: Int = 39) {
    val colores = alerta.severidad.colores()
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(RoundedCornerShape((tamano * 0.3f).dp))
            .background(colores.fondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = alerta.tipo.icono(),
            contentDescription = alerta.tipo.etiqueta,
            tint = colores.texto,
            modifier = Modifier.size((tamano * 0.53f).dp)
        )
    }
}

@Composable
fun AlertaItem(alerta: Alerta, onClick: () -> Unit, modifier: Modifier = Modifier) {
    AgroCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconoAlerta(alerta)
            Column(modifier = Modifier.weight(1f)) {
                Text(alerta.titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                val detalle = if (alerta.revisadaPor != null && alerta.estado == EstadoGestion.REVISADA)
                    "${alerta.ubicacion.substringBefore(" ·")} · Revisada por ${alerta.revisadaPor} · ${alerta.revisadaA}"
                else
                    "${alerta.ubicacion.substringBefore(" ·")} · ${alerta.momento}"
                Text(
                    detalle,
                    fontSize = 12.sp,
                    color = Color(0xFF6F786F),
                    modifier = Modifier.padding(top = 3.dp, bottom = 6.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    ChipSeveridad(alerta.severidad)
                    ChipGestion(alerta.estado)
                }
            }
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF6F786F),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
