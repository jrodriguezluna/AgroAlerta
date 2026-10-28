package com.example.agroalerta.data.model

/** Estado de una lectura de sensor o de un cuartel completo. */
enum class EstadoSensor { NORMAL, PRECAUCION, CRITICO, SIN_DATOS }

enum class EstadoEquipo(val etiqueta: String) {
    ENCENDIDO("Encendido"),
    APAGADO("Apagado"),
    FALLA("Falla"),
    MANTENCION("Mantención"),
    SIN_RESPUESTA("Sin respuesta")
}

/** Cómo va la gestión de una alerta (trazabilidad). */
enum class EstadoGestion(val etiqueta: String) {
    NUEVA("Nueva"),
    REVISADA("Revisada"),
    RESUELTA("Resuelta")
}

enum class Severidad(val etiqueta: String) {
    CRITICA("Crítica"),
    ADVERTENCIA("Advertencia"),
    INFORMATIVA("Informativa")
}

enum class TipoAlerta(val etiqueta: String) {
    HELADA("Helada"),
    FUGA("Fuga / obstrucción"),
    NIVEL_AGUA("Nivel de agua"),
    HUMEDAD("Humedad de suelo")
}

enum class Tendencia(val simbolo: String) {
    SUBE("↑"),
    BAJA("↓"),
    ESTABLE("→")
}

enum class TipoSensor { HUMEDAD_SUELO, TEMPERATURA, HUMEDAD_RELATIVA, RADIACION_SOLAR, CAUDAL }

enum class TipoSistema { RIEGO, ANTIHELADAS, ESTANQUE, CLIMA }

data class Sensor(
    val tipo: TipoSensor,
    val nombre: String,
    val valor: String,
    val unidad: String,
    val estado: EstadoSensor,
    val umbral: String,
    val tendencia: Tendencia,
    val historial: List<Float>
)

data class Cuartel(
    val id: Int,
    val nombre: String,
    val estacion: String,
    val estado: EstadoSensor,
    /** Texto con el dato relevante (solo lo ve el supervisor). */
    val resumenSupervisor: String? = null,
    val sensores: List<Sensor>
)

/** Tarjeta de la vista general del predio (riego, anti-heladas, estanque, clima). */
data class Sistema(
    val tipo: TipoSistema,
    val nombre: String,
    val valor: String,
    val unidad: String,
    val estado: EstadoSensor,
    /** Solo para el estanque: 0f..1f. */
    val porcentaje: Float? = null
)

data class Equipo(
    val nombre: String,
    val ubicacion: String,
    val estado: EstadoEquipo
)

data class GrupoEquipos(val titulo: String, val equipos: List<Equipo>)

data class AccionSugerida(
    val titulo: String,
    val detalleSupervisor: String,
    val detalleTecnico: String
)

data class Alerta(
    val id: Int,
    val titulo: String,
    val tipo: TipoAlerta,
    val severidad: Severidad,
    val estado: EstadoGestion,
    val cuartelId: Int?,
    val ubicacion: String,
    val dia: String,
    val hora: String,
    val descripcion: String,
    val avisoTecnico: String,
    val instruccionTecnico: String,
    val valorMedido: String?,
    val umbral: String?,
    val unidad: String = "",
    val accion: AccionSugerida?,
    val detectadaA: String,
    val notificadaA: String,
    val revisadaPor: String? = null,
    val revisadaA: String? = null,
    val resueltaA: String? = null
) {
    val momento: String get() = "$dia · $hora"
}

/** Un equipo "Sin respuesta" cuenta como "Sin datos"; "Falla" como crítico; el resto como normal. */
fun EstadoEquipo.comoEstadoSensor(): EstadoSensor = when (this) {
    EstadoEquipo.FALLA -> EstadoSensor.CRITICO
    EstadoEquipo.SIN_RESPUESTA -> EstadoSensor.SIN_DATOS
    else -> EstadoSensor.NORMAL
}
