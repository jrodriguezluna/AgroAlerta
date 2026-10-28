package com.example.agroalerta.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.material.icons.rounded.WbCloudy
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.agroalerta.data.local.Rol
import com.example.agroalerta.data.model.EstadoSensor
import com.example.agroalerta.data.model.Severidad
import com.example.agroalerta.data.model.TipoAlerta
import com.example.agroalerta.data.model.TipoSensor
import com.example.agroalerta.data.model.TipoSistema
import com.example.agroalerta.ui.theme.ColoresAgro
import com.example.agroalerta.ui.theme.ColoresEstado

/*
 * Textos que ve el TÉCNICO. Si el profe pide exactamente "Bien" / "Mal",
 * solo hay que cambiar estas dos constantes.
 */
const val TEXTO_TECNICO_OK = "Normal"
const val TEXTO_TECNICO_REVISAR = "Revisar"

data class VisualEstado(val icono: ImageVector, val texto: String, val colores: ColoresEstado)

/**
 * El técnico no distingue "Precaución" de "Normal": solo necesita saber si algo está bien,
 * si hay que revisarlo o si no hay datos.
 */
fun EstadoSensor.paraRol(rol: Rol): EstadoSensor =
    if (rol == Rol.TECNICO && this == EstadoSensor.PRECAUCION) EstadoSensor.NORMAL else this

fun EstadoSensor.visual(rol: Rol): VisualEstado {
    val tecnico = rol == Rol.TECNICO
    return when (paraRol(rol)) {
        EstadoSensor.NORMAL -> VisualEstado(Icons.Rounded.CheckCircle, if (tecnico) TEXTO_TECNICO_OK else "Normal", ColoresAgro.normal)
        EstadoSensor.PRECAUCION -> VisualEstado(Icons.Rounded.Warning, "Precaución", ColoresAgro.precaucion)
        EstadoSensor.CRITICO -> VisualEstado(Icons.Rounded.Block, if (tecnico) TEXTO_TECNICO_REVISAR else "Crítico", ColoresAgro.critico)
        EstadoSensor.SIN_DATOS -> VisualEstado(Icons.Rounded.CloudOff, "Sin datos", ColoresAgro.sinDatos)
    }
}

fun EstadoSensor.tituloResumen(): String = when (this) {
    EstadoSensor.NORMAL -> "Operando con normalidad"
    EstadoSensor.PRECAUCION -> "En observación"
    EstadoSensor.CRITICO -> "Requiere atención"
    EstadoSensor.SIN_DATOS -> "Sin conexión con la estación"
}

fun Severidad.colores(): ColoresEstado = when (this) {
    Severidad.CRITICA -> ColoresAgro.critico
    Severidad.ADVERTENCIA -> ColoresAgro.precaucion
    Severidad.INFORMATIVA -> ColoresAgro.normal
}

fun TipoAlerta.icono(): ImageVector = when (this) {
    TipoAlerta.HELADA -> Icons.Rounded.AcUnit
    TipoAlerta.FUGA -> Icons.Rounded.Speed
    TipoAlerta.NIVEL_AGUA -> Icons.Rounded.Waves
    TipoAlerta.HUMEDAD -> Icons.Rounded.WaterDrop
}

fun TipoSensor.icono(): ImageVector = when (this) {
    TipoSensor.HUMEDAD_SUELO -> Icons.Rounded.WaterDrop
    TipoSensor.TEMPERATURA -> Icons.Rounded.Thermostat
    TipoSensor.HUMEDAD_RELATIVA -> Icons.Rounded.Opacity
    TipoSensor.RADIACION_SOLAR -> Icons.Rounded.WbSunny
    TipoSensor.CAUDAL -> Icons.Rounded.Speed
}

fun TipoSistema.icono(): ImageVector = when (this) {
    TipoSistema.RIEGO -> Icons.Rounded.WaterDrop
    TipoSistema.ANTIHELADAS -> Icons.Rounded.AcUnit
    TipoSistema.ESTANQUE -> Icons.Rounded.Waves
    TipoSistema.CLIMA -> Icons.Rounded.WbCloudy
}
