package com.example.agroalerta.ui.alertas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.Alerta
import com.example.agroalerta.data.model.EstadoGestion
import com.example.agroalerta.data.model.TipoAlerta
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.AlertaItem
import com.example.agroalerta.ui.components.StaleBanner
import com.example.agroalerta.ui.theme.BordeSuave
import com.example.agroalerta.ui.theme.RojoCritico
import com.example.agroalerta.ui.theme.VerdePrimario

// null = "Todas". La humedad de suelo solo aparece en "Todas" (igual que en el diseño de Figma).
private val Filtros: List<TipoAlerta?> = listOf(null, TipoAlerta.HELADA, TipoAlerta.FUGA, TipoAlerta.NIVEL_AGUA)

@Composable
fun AlertasScreen(
    rol: Rol,
    iniciales: String,
    alertas: List<Alerta>,
    desactualizado: Boolean,
    onPerfil: () -> Unit,
    onAbrirAlerta: (Int) -> Unit,
    onAlternarDesactualizado: () -> Unit
) {
    var pestana by rememberSaveable { mutableStateOf(0) }   // 0 = Activas, 1 = Historial
    var filtro by rememberSaveable { mutableStateOf(0) }    // índice en Filtros

    val tipoFiltrado = Filtros[filtro]
    val deLaPestana = alertas.filter { alerta ->
        val esActiva = alerta.estado != EstadoGestion.RESUELTA
        (if (pestana == 0) esActiva else !esActiva) && (tipoFiltrado == null || alerta.tipo == tipoFiltrado)
    }
    val cantidadActivas = alertas.count { it.estado != EstadoGestion.RESUELTA }

    Column(Modifier.fillMaxSize()) {
        AgroTopBar(titulo = "Alertas", rol = rol, iniciales = iniciales, onPerfil = onPerfil)
        if (desactualizado) StaleBanner(compacto = true)

        Row(Modifier.fillMaxWidth()) {
            Pestana("Activas", pestana == 0, cantidadActivas, { pestana = 0 }, Modifier.weight(1f))
            Pestana("Historial", pestana == 1, null, { pestana = 1 }, Modifier.weight(1f))
        }
        HorizontalDivider(color = BordeSuave)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Filtros.forEachIndexed { indice, tipo ->
                    FilterChip(
                        selected = filtro == indice,
                        onClick = { filtro = indice },
                        label = { Text(tipo?.etiqueta ?: "Todas", fontSize = 12.sp) },
                        shape = RoundedCornerShape(9.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFDCEFD9),
                            selectedLabelColor = Color(0xFF195F29)
                        )
                    )
                }
            }

            if (deLaPestana.isEmpty()) {
                EstadoVacio(if (pestana == 0) "Sin alertas activas" else "Sin eventos en el historial")
            } else if (pestana == 0) {
                Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    deLaPestana.forEach { AlertaItem(it, onClick = { onAbrirAlerta(it.id) }) }
                }
            } else {
                // Historial agrupado por día (HOY, AYER...)
                deLaPestana.groupBy { it.dia }.forEach { (dia, delDia) ->
                    Text(
                        dia.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF6F786F),
                        modifier = Modifier.padding(start = 2.dp, top = 10.dp, bottom = 6.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        delDia.forEach { AlertaItem(it, onClick = { onAbrirAlerta(it.id) }) }
                    }
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
private fun Pestana(
    texto: String,
    seleccionada: Boolean,
    contador: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                texto,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (seleccionada) Color(0xFF236C30) else Color(0xFF697269)
            )
            if (contador != null && contador > 0) {
                Text(
                    contador.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(RojoCritico)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(if (seleccionada) VerdePrimario else Color.Transparent)
        )
    }
}

@Composable
private fun EstadoVacio(mensaje: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color(0xFF75A979), modifier = Modifier.size(48.dp))
        Text(mensaje, fontSize = 14.sp, color = Color(0xFF59635A))
    }
}
