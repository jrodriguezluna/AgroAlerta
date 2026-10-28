package com.example.agroalerta.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.Cuartel
import com.example.agroalerta.data.model.EstadoSensor
import com.example.agroalerta.data.model.Sistema
import com.example.agroalerta.ui.components.AgroCard
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.EncabezadoSeccion
import com.example.agroalerta.ui.components.StaleBanner
import com.example.agroalerta.ui.components.StatusChip
import com.example.agroalerta.ui.components.icono
import com.example.agroalerta.ui.components.paraRol
import com.example.agroalerta.ui.components.visual
import com.example.agroalerta.ui.theme.RojoCritico
import com.example.agroalerta.ui.theme.VerdePrimario

@Composable
fun HomeScreen(
    rol: Rol,
    iniciales: String,
    cuarteles: List<Cuartel>,
    sistemas: List<Sistema>,
    alertasActivas: Int,
    desactualizado: Boolean,
    onPerfil: () -> Unit,
    onIrAlertas: () -> Unit,
    onAbrirCuartel: (Int) -> Unit,
    onAlternarDesactualizado: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        AgroTopBar(
            titulo = "Agrícola FRUTIS · Buin",
            rol = rol,
            iniciales = iniciales,
            onPerfil = onPerfil
        )
        if (desactualizado) StaleBanner()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
        ) {
            TarjetaEstadoGeneral(requiereAtencion = cuarteles.any { it.estado == EstadoSensor.CRITICO })

            Spacer(Modifier.height(12.dp))
            MapaPredio(cuarteles = cuarteles, rol = rol, onAbrirCuartel = onAbrirCuartel)

            Spacer(Modifier.height(12.dp))
            ContadorAlertas(cantidad = alertasActivas, onClick = onIrAlertas)

            EncabezadoSeccion("Sistemas", detalle = "Vista general")
            sistemas.chunked(2).forEachIndexed { indice, fila ->
                if (indice > 0) Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    fila.forEach { sistema ->
                        TarjetaSistema(sistema, rol, Modifier.weight(1f).fillMaxHeight())
                    }
                    if (fila.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            if (!desactualizado) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Rounded.Sync, null, tint = Color(0xFF6D766D), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(5.dp))
                    Text("Última actualización: hace 2 min", fontSize = 12.sp, color = Color(0xFF6D766D))
                }
            } else {
                Spacer(Modifier.height(8.dp))
            }

            // Solo para la demostración: alterna entre datos al día y datos desactualizados.
            TextButton(onClick = onAlternarDesactualizado, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Icon(
                    if (desactualizado) Icons.Rounded.CloudDone else Icons.Rounded.CloudOff,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    if (desactualizado) "Ver datos actualizados" else "Simular datos desactualizados",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun TarjetaEstadoGeneral(requiereAtencion: Boolean) {
    val colores = if (requiereAtencion) listOf(Color(0xFF9F1F24), RojoCritico)
    else listOf(Color(0xFF1B5E20), VerdePrimario)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colores))
            .padding(17.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color.White.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (requiereAtencion) Icons.Rounded.Warning else Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Column {
            Text(
                "ESTADO GENERAL",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Text(
                if (requiereAtencion) "Atención requerida" else "Predio operativo",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                if (requiereAtencion) "Hay situaciones que necesitan revisión." else "Todos los sistemas funcionan con normalidad.",
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/** Mapa esquemático del predio: 4 bloques (uno por cuartel) coloreados según su estado. */
@Composable
private fun MapaPredio(cuarteles: List<Cuartel>, rol: Rol, onAbrirCuartel: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        cuarteles.chunked(2).forEach { fila ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                fila.forEach { cuartel ->
                    val visual = cuartel.estado.visual(rol)
                    Surface(
                        onClick = { onAbrirCuartel(cuartel.id) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = visual.colores.fondo
                    ) {
                        Row(
                            modifier = Modifier
                                .heightIn(min = 47.dp)
                                .padding(horizontal = 11.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                cuartel.nombre,
                                color = visual.colores.texto,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(visual.icono, contentDescription = visual.texto, tint = visual.colores.texto, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                if (fila.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ContadorAlertas(cantidad: Int, onClick: () -> Unit) {
    val hayAlertas = cantidad > 0
    val fondo = if (hayAlertas) Color(0xFFFFF6F4) else Color(0xFFEEF7EC)
    val borde = if (hayAlertas) Color(0xFFF1C8C8) else Color(0xFFD5E8D2)
    val texto = if (hayAlertas) Color(0xFF971F24) else Color(0xFF1F6B2D)

    AgroCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        container = fondo,
        borde = borde,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 52.dp)
                .padding(horizontal = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Icon(Icons.Rounded.NotificationsActive, contentDescription = null, tint = texto)
                Text(
                    text = when (cantidad) {
                        0 -> "Sin alertas activas"
                        1 -> "1 alerta activa"
                        else -> "$cantidad alertas activas"
                    },
                    color = texto,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = texto)
        }
    }
}

@Composable
private fun TarjetaSistema(sistema: Sistema, rol: Rol, modifier: Modifier = Modifier) {
    AgroCard(modifier = modifier, shape = RoundedCornerShape(17.dp)) {
        Column(
            modifier = Modifier
                .heightIn(min = 132.dp)
                .padding(13.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE5F2E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(sistema.tipo.icono(), contentDescription = null, tint = Color(0xFF266E30))
            }
            Spacer(Modifier.height(8.dp))
            Text(sistema.nombre, fontSize = 13.sp, color = Color(0xFF59635A))
            Spacer(Modifier.height(4.dp))

            // El técnico no ve valores numéricos, solo el estado.
            if (rol == Rol.SUPERVISOR) {
                if (sistema.porcentaje != null) {
                    MedidorEstanque(sistema.porcentaje, "${sistema.valor}${sistema.unidad}")
                } else {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(sistema.valor, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1C2A1E))
                        Text(" ${sistema.unidad}", fontSize = 11.sp, color = Color(0xFF677268), modifier = Modifier.padding(bottom = 3.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
            } else {
                Spacer(Modifier.height(4.dp))
            }
            StatusChip(sistema.estado, rol)
        }
    }
}

/** Medidor del estanque: un recuadro que se llena de abajo hacia arriba. */
@Composable
private fun MedidorEstanque(fraccion: Float, texto: String) {
    val forma = RoundedCornerShape(9.dp)
    Box(
        modifier = Modifier
            .size(width = 66.dp, height = 34.dp)
            .clip(forma)
            .border(2.dp, Color(0xFF9BC39C), forma),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(fraccion.coerceIn(0f, 1f))
                .background(Color(0xFFCDE8CB))
        )
        Text(texto, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF174F22))
    }
}
