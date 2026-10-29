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
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.WaterDrop
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
import com.example.agroalerta.data.model.Actuador
import com.example.agroalerta.data.model.Alerta
import com.example.agroalerta.data.model.Cuartel
import com.example.agroalerta.data.model.EstadoEquipo
import com.example.agroalerta.data.model.EstadoSensor
import com.example.agroalerta.data.model.Sensor
import com.example.agroalerta.data.model.TipoActuador
import com.example.agroalerta.data.model.comoEstadoSensor
import com.example.agroalerta.ui.components.AgroCard
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.EncabezadoSeccion
import com.example.agroalerta.ui.components.IconoAlerta
import com.example.agroalerta.ui.components.Sparkline
import com.example.agroalerta.ui.components.StatusChip
import com.example.agroalerta.ui.components.colores
import com.example.agroalerta.ui.components.icono
import com.example.agroalerta.ui.components.paraRol
import com.example.agroalerta.ui.components.tituloResumen
import com.example.agroalerta.ui.components.visual
import com.example.agroalerta.ui.theme.ColoresAgro
import com.example.agroalerta.ui.theme.ColoresEstado

@Composable
fun CuartelDetalleScreen(
    cuartel: Cuartel,
    alertas: List<Alerta>,
    rol: Rol,
    desactualizado: Boolean,
    onAtras: () -> Unit,
    onAbrirAlerta: (Int) -> Unit
) {
    val visual = cuartel.estado.visual(rol)

    Column(Modifier.fillMaxSize()) {
        AgroTopBar(titulo = cuartel.nombre, rol = rol, onAtras = onAtras)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
        ) {
            // Resumen del cuartel
            AgroCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                container = visual.colores.suave,
                borde = visual.colores.borde
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 13.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            cuartel.estacion.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = visual.colores.texto.copy(alpha = 0.75f)
                        )
                        Text(
                            cuartel.estado.paraRol(rol).tituloResumen(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = visual.colores.texto,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                    StatusChip(cuartel.estado, rol)
                }
            }

            EncabezadoSeccion("Sensores", detalle = if (desactualizado) "Hace 25 min" else "Hace 2 min")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cuartel.sensores.forEach { FilaSensor(it, rol) }
            }

            if (cuartel.actuadores.isNotEmpty()) {
                EncabezadoSeccion(
                    "Actuadores",
                    detalle = "${cuartel.actuadores.size} monitoreados"
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    cuartel.actuadores.forEach { FilaActuador(it, rol) }
                }
            }

            EncabezadoSeccion("Alertas de este cuartel")
            if (alertas.isEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Rounded.CheckCircle, null, tint = Color(0xFF1F6B2D), modifier = Modifier.size(18.dp))
                    Text("Sin alertas activas en este cuartel", fontSize = 13.sp, color = Color(0xFF59635A))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    alertas.take(2).forEach { alerta ->
                        val colores = alerta.severidad.colores()
                        AgroCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            container = colores.suave,
                            borde = colores.borde,
                            onClick = { onAbrirAlerta(alerta.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                IconoAlerta(alerta, tamano = 36)
                                Column(Modifier.weight(1f)) {
                                    Text(alerta.titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colores.texto)
                                    Text(alerta.momento, fontSize = 11.sp, color = colores.texto.copy(alpha = 0.75f), modifier = Modifier.padding(top = 3.dp))
                                }
                                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = colores.texto)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaActuador(actuador: Actuador, rol: Rol) {
    val (colores, icono) = aparienciaActuador(actuador)
    AgroCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
        Row(
            modifier = Modifier.padding(11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colores.fondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = colores.texto)
            }
            Text(actuador.nombre, modifier = Modifier.weight(1f), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            StatusChip(
                estado = actuador.estado.comoEstadoSensor(),
                rol = rol,
                etiqueta = if (rol == Rol.SUPERVISOR) actuador.estado.etiqueta else null
            )
        }
    }
}

private fun aparienciaActuador(actuador: Actuador): Pair<ColoresEstado, ImageVector> {
    val icono = when (actuador.tipo) {
        TipoActuador.ELECTROVALVULA -> Icons.Rounded.WaterDrop
        TipoActuador.BOMBA -> Icons.Rounded.PowerSettingsNew
        TipoActuador.TORRE_ANTIHELO -> Icons.Rounded.AcUnit
    }
    return when (actuador.estado) {
        EstadoEquipo.ENCENDIDO -> ColoresAgro.normal to icono
        EstadoEquipo.APAGADO -> ColoresAgro.apagado to icono
        EstadoEquipo.FALLA -> ColoresAgro.critico to Icons.Rounded.Error
        EstadoEquipo.MANTENCION -> ColoresAgro.mantencion to Icons.Rounded.Build
        EstadoEquipo.SIN_RESPUESTA -> ColoresAgro.sinDatos to Icons.Rounded.CloudOff
    }
}

@Composable
private fun FilaSensor(sensor: Sensor, rol: Rol) {
    val supervisor = rol == Rol.SUPERVISOR
    val critico = sensor.estado == EstadoSensor.CRITICO
    val colorTendencia = when (sensor.estado) {
        EstadoSensor.CRITICO -> Color(0xFFA32329)
        EstadoSensor.PRECAUCION -> Color(0xFF8A6000)
        else -> Color(0xFF4F6B52)
    }

    AgroCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
        Row(
            modifier = Modifier.padding(11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFE5F2E2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(sensor.tipo.icono(), contentDescription = null, tint = Color(0xFF266E30))
            }

            Column(Modifier.weight(1f)) {
                Text(sensor.nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                if (supervisor) {
                    Text("Umbral: ${sensor.umbral}", fontSize = 11.sp, color = Color(0xFF737D73))
                }
            }

            if (supervisor) {
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            sensor.valor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (critico) Color(0xFFA32329) else Color(0xFF1A1C19)
                        )
                        if (sensor.unidad.isNotEmpty()) {
                            Text(" ${sensor.unidad}", fontSize = 11.sp, color = Color(0xFF677268), modifier = Modifier.padding(bottom = 2.dp))
                        }
                    }
                    if (sensor.historial.size > 1) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(sensor.tendencia.simbolo, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = colorTendencia)
                            Sparkline(sensor.historial, colorTendencia)
                        }
                    }
                    Spacer(Modifier.height(3.dp))
                    StatusChip(sensor.estado, rol, compacto = true)
                }
            } else {
                StatusChip(sensor.estado, rol)
            }
        }
    }
}
