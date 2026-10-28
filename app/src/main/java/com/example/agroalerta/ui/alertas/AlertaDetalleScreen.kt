package com.example.agroalerta.ui.alertas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HomeRepairService
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.Alerta
import com.example.agroalerta.data.model.EstadoGestion
import com.example.agroalerta.ui.components.AgroCard
import com.example.agroalerta.ui.components.AgroTopBar
import com.example.agroalerta.ui.components.BotonPrimario
import com.example.agroalerta.ui.components.CajaInformativa
import com.example.agroalerta.ui.components.ChipGestion
import com.example.agroalerta.ui.components.ChipSeveridad
import com.example.agroalerta.ui.components.EncabezadoSeccion
import com.example.agroalerta.ui.components.IconoAlerta
import com.example.agroalerta.ui.components.colores
import com.example.agroalerta.ui.theme.BordeSuave

@Composable
fun AlertaDetalleScreen(
    alerta: Alerta,
    rol: Rol,
    onAtras: () -> Unit,
    onMarcarRevisada: () -> Unit
) {
    val supervisor = rol == Rol.SUPERVISOR
    val colores = alerta.severidad.colores()

    Column(Modifier.fillMaxSize()) {
        AgroTopBar(titulo = "Detalle de alerta", rol = rol, onAtras = onAtras)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
        ) {
            // Cabecera
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                IconoAlerta(alerta, tamano = 58)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    ChipSeveridad(alerta.severidad)
                    ChipGestion(alerta.estado)
                }
                Text(
                    alerta.titulo,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    alerta.descripcion,
                    fontSize = 13.sp,
                    color = Color(0xFF687168),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }

            // Ubicación y fecha
            AgroCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) {
                Column(Modifier.padding(horizontal = 14.dp)) {
                    DatoEtiquetado("UBICACIÓN", alerta.ubicacion)
                    HorizontalDivider(color = Color(0xFFE7EBE4))
                    DatoEtiquetado("FECHA Y HORA", alerta.momento)
                }
            }

            if (supervisor) {
                // Valor medido vs. umbral (solo supervisor)
                if (alerta.valorMedido != null && alerta.umbral != null) {
                    Spacer(Modifier.height(13.dp))
                    CajaColor(colores.suave) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Medida("VALOR MEDIDO", alerta.valorMedido, alerta.unidad, colores.texto, Modifier.weight(1f))
                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, tint = colores.texto, modifier = Modifier.size(20.dp))
                            Medida("UMBRAL", alerta.umbral, alerta.unidad, colores.texto, Modifier.weight(1f))
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))
                when (alerta.estado) {
                    EstadoGestion.NUEVA -> BotonPrimario(
                        texto = "Marcar como revisada",
                        icono = Icons.Rounded.TaskAlt,
                        onClick = onMarcarRevisada
                    )
                    EstadoGestion.REVISADA -> CajaInformativa(
                        icono = Icons.Rounded.Verified,
                        titulo = "Alerta revisada",
                        detalle = "Revisada por ${alerta.revisadaPor} · ${alerta.revisadaA}"
                    )
                    EstadoGestion.RESUELTA -> CajaInformativa(
                        icono = Icons.Rounded.CheckCircle,
                        titulo = "Alerta resuelta",
                        detalle = "Resuelta a las ${alerta.resueltaA}"
                    )
                }
            } else {
                // El técnico solo ve una descripción simple, sin valores ni botones de acción.
                Spacer(Modifier.height(14.dp))
                CajaInformativa(
                    icono = Icons.Rounded.Info,
                    titulo = alerta.avisoTecnico,
                    detalle = alerta.instruccionTecnico
                )
            }

            alerta.accion?.let { accion ->
                EncabezadoSeccion("Acción sugerida")
                AgroCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    container = Color(0xFFE8F2E5),
                    borde = Color(0xFFD2E5D0)
                ) {
                    Row(
                        modifier = Modifier.padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        Icon(Icons.Rounded.HomeRepairService, null, tint = Color(0xFF345D38), modifier = Modifier.size(27.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Text(accion.titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF345D38))
                            Text(
                                if (supervisor) accion.detalleSupervisor else accion.detalleTecnico,
                                fontSize = 12.sp,
                                color = Color(0xFF5D6D5E)
                            )
                        }
                    }
                }
            }

            // Línea de tiempo (trazabilidad)
            EncabezadoSeccion("Línea de tiempo")
            val revisada = alerta.estado != EstadoGestion.NUEVA
            val resuelta = alerta.estado == EstadoGestion.RESUELTA
            Column(Modifier.padding(horizontal = 7.dp)) {
                PasoTimeline("Detectada", alerta.detectadaA, completado = true, ultimo = false)
                PasoTimeline("Notificada", alerta.notificadaA, completado = true, ultimo = false)
                PasoTimeline(
                    titulo = if (revisada) "Revisada por ${alerta.revisadaPor}" else "Revisada",
                    detalle = if (revisada) alerta.revisadaA ?: "" else "Pendiente",
                    completado = revisada,
                    ultimo = false
                )
                PasoTimeline(
                    titulo = "Resuelta",
                    detalle = if (resuelta) alerta.resueltaA ?: "" else "Pendiente",
                    completado = resuelta,
                    ultimo = true
                )
            }
        }
    }
}

@Composable
private fun CajaColor(color: Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(color)
    ) { content() }
}

@Composable
private fun DatoEtiquetado(etiqueta: String, valor: String) {
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

@Composable
private fun Medida(etiqueta: String, valor: String, unidad: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(etiqueta, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, color = color.copy(alpha = 0.8f))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(valor, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = color)
            Text(" $unidad", fontSize = 12.sp, color = color, modifier = Modifier.padding(bottom = 3.dp))
        }
    }
}

/** Un paso de la línea de tiempo. Los pasos pendientes van en gris con línea punteada. */
@Composable
private fun PasoTimeline(titulo: String, detalle: String, completado: Boolean, ultimo: Boolean) {
    val colorLinea = if (completado) Color(0xFF75A979) else Color(0xFFAAB1AA)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 47.dp)
            .drawBehind {
                if (!ultimo) {
                    val x = 12.5.dp.toPx()
                    drawLine(
                        color = colorLinea,
                        start = Offset(x, 26.dp.toPx()),
                        end = Offset(x, size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = if (completado) null else PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                    )
                }
            },
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .background(if (completado) Color(0xFFDCEFD9) else Color(0xFFE7EAE6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (completado) Icons.Rounded.Check else Icons.Rounded.Schedule,
                contentDescription = null,
                tint = if (completado) Color(0xFF23692F) else Color(0xFF707970),
                modifier = Modifier.size(15.dp)
            )
        }
        Column(Modifier.padding(top = 1.dp)) {
            Text(titulo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(detalle, fontSize = 11.sp, color = Color(0xFF707970), modifier = Modifier.padding(top = 2.dp))
        }
    }
}
