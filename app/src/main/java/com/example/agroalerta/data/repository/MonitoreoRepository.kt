package com.example.agroalerta.data.repository

import com.example.agroalerta.data.model.AccionSugerida
import com.example.agroalerta.data.model.Alerta
import com.example.agroalerta.data.model.Cuartel
import com.example.agroalerta.data.model.Equipo
import com.example.agroalerta.data.model.EstadoEquipo
import com.example.agroalerta.data.model.EstadoGestion
import com.example.agroalerta.data.model.EstadoSensor
import com.example.agroalerta.data.model.GrupoEquipos
import com.example.agroalerta.data.model.Sensor
import com.example.agroalerta.data.model.Severidad
import com.example.agroalerta.data.model.Sistema
import com.example.agroalerta.data.model.Tendencia
import com.example.agroalerta.data.model.TipoAlerta
import com.example.agroalerta.data.model.TipoSensor
import com.example.agroalerta.data.model.TipoSistema

/**
 * Fuente de datos del monitoreo.
 *
 * En esta entrega la implementación es [MonitoreoRepositoryFalso] (datos hardcodeados).
 * En la siguiente, se crea otra implementación con Retrofit + Room (caché) y el
 * ViewModel no cambia: solo se cambia qué implementación usa.
 */
interface MonitoreoRepository {
    fun cuarteles(): List<Cuartel>
    fun sistemas(): List<Sistema>
    fun gruposEquipos(): List<GrupoEquipos>
    fun alertas(): List<Alerta>
}

class MonitoreoRepositoryFalso : MonitoreoRepository {

    override fun cuarteles(): List<Cuartel> = listOf(
        Cuartel(
            id = 1, nombre = "Cuartel 1", estacion = "Estación Norte", estado = EstadoSensor.NORMAL,
            sensores = listOf(
                Sensor(TipoSensor.HUMEDAD_SUELO, "Humedad de suelo", "52", "%", EstadoSensor.NORMAL, "mín. 35 %", Tendencia.ESTABLE, listOf(51f, 52f, 53f, 52f, 52f, 52f)),
                Sensor(TipoSensor.TEMPERATURA, "Temperatura", "8,1", "°C", EstadoSensor.NORMAL, "mín. 3 °C", Tendencia.ESTABLE, listOf(7.6f, 7.8f, 8.0f, 8.1f, 8.1f, 8.1f)),
                Sensor(TipoSensor.HUMEDAD_RELATIVA, "Humedad relativa", "61", "%", EstadoSensor.NORMAL, "40–80 %", Tendencia.ESTABLE, listOf(60f, 61f, 61f, 62f, 61f, 61f)),
                Sensor(TipoSensor.RADIACION_SOLAR, "Radiación solar", "640", "W/m²", EstadoSensor.NORMAL, "máx. 900 W/m²", Tendencia.SUBE, listOf(420f, 480f, 540f, 590f, 620f, 640f)),
                Sensor(TipoSensor.CAUDAL, "Caudal", "16", "L/min", EstadoSensor.NORMAL, "mín. 10 L/min", Tendencia.ESTABLE, listOf(16f, 16f, 15f, 16f, 16f, 16f))
            )
        ),
        Cuartel(
            id = 2, nombre = "Cuartel 2", estacion = "Estación Norte", estado = EstadoSensor.PRECAUCION,
            resumenSupervisor = "Humedad 36 % · mín. 35 %",
            sensores = listOf(
                Sensor(TipoSensor.HUMEDAD_SUELO, "Humedad de suelo", "36", "%", EstadoSensor.PRECAUCION, "mín. 35 %", Tendencia.BAJA, listOf(42f, 40f, 39f, 38f, 37f, 36f)),
                Sensor(TipoSensor.TEMPERATURA, "Temperatura", "7,9", "°C", EstadoSensor.NORMAL, "mín. 3 °C", Tendencia.ESTABLE, listOf(7.5f, 7.7f, 7.8f, 7.9f, 7.9f, 7.9f)),
                Sensor(TipoSensor.HUMEDAD_RELATIVA, "Humedad relativa", "64", "%", EstadoSensor.NORMAL, "40–80 %", Tendencia.ESTABLE, listOf(63f, 64f, 64f, 65f, 64f, 64f)),
                Sensor(TipoSensor.RADIACION_SOLAR, "Radiación solar", "—", "", EstadoSensor.SIN_DATOS, "máx. 900 W/m²", Tendencia.ESTABLE, emptyList()),
                Sensor(TipoSensor.CAUDAL, "Caudal", "15", "L/min", EstadoSensor.NORMAL, "mín. 10 L/min", Tendencia.ESTABLE, listOf(15f, 15f, 16f, 15f, 15f, 15f))
            )
        ),
        Cuartel(
            id = 3, nombre = "Cuartel 3", estacion = "Estación Central", estado = EstadoSensor.CRITICO,
            resumenSupervisor = "Humedad 31 % · mín. 35 %",
            sensores = listOf(
                Sensor(TipoSensor.HUMEDAD_SUELO, "Humedad de suelo", "31", "%", EstadoSensor.CRITICO, "mín. 35 %", Tendencia.BAJA, listOf(40f, 38f, 36f, 34f, 32f, 31f)),
                Sensor(TipoSensor.TEMPERATURA, "Temperatura", "7,4", "°C", EstadoSensor.NORMAL, "mín. 3 °C", Tendencia.ESTABLE, listOf(7.2f, 7.3f, 7.4f, 7.4f, 7.4f, 7.4f)),
                Sensor(TipoSensor.HUMEDAD_RELATIVA, "Humedad relativa", "68", "%", EstadoSensor.NORMAL, "40–80 %", Tendencia.SUBE, listOf(60f, 62f, 64f, 65f, 67f, 68f)),
                Sensor(TipoSensor.RADIACION_SOLAR, "Radiación solar", "—", "", EstadoSensor.SIN_DATOS, "máx. 900 W/m²", Tendencia.ESTABLE, emptyList()),
                Sensor(TipoSensor.CAUDAL, "Caudal", "14", "L/min", EstadoSensor.PRECAUCION, "mín. 10 L/min", Tendencia.BAJA, listOf(18f, 17f, 16f, 15f, 14f, 14f))
            )
        ),
        Cuartel(
            id = 4, nombre = "Cuartel 4", estacion = "Estación Sur", estado = EstadoSensor.CRITICO,
            resumenSupervisor = "Temperatura 1,8 °C · mín. 3 °C",
            sensores = listOf(
                Sensor(TipoSensor.HUMEDAD_SUELO, "Humedad de suelo", "44", "%", EstadoSensor.NORMAL, "mín. 35 %", Tendencia.ESTABLE, listOf(44f, 44f, 45f, 44f, 44f, 44f)),
                Sensor(TipoSensor.TEMPERATURA, "Temperatura", "1,8", "°C", EstadoSensor.CRITICO, "mín. 3 °C", Tendencia.BAJA, listOf(4.2f, 3.8f, 3.1f, 2.6f, 2.2f, 1.8f)),
                Sensor(TipoSensor.HUMEDAD_RELATIVA, "Humedad relativa", "82", "%", EstadoSensor.PRECAUCION, "40–80 %", Tendencia.SUBE, listOf(70f, 73f, 76f, 78f, 80f, 82f)),
                Sensor(TipoSensor.RADIACION_SOLAR, "Radiación solar", "120", "W/m²", EstadoSensor.NORMAL, "máx. 900 W/m²", Tendencia.BAJA, listOf(260f, 220f, 180f, 150f, 130f, 120f)),
                Sensor(TipoSensor.CAUDAL, "Caudal", "13", "L/min", EstadoSensor.NORMAL, "mín. 10 L/min", Tendencia.ESTABLE, listOf(13f, 13f, 14f, 13f, 13f, 13f))
            )
        )
    )

    override fun sistemas(): List<Sistema> = listOf(
        Sistema(TipoSistema.RIEGO, "Riego", "3/4", "válvulas", EstadoSensor.CRITICO),
        Sistema(TipoSistema.ANTIHELADAS, "Anti-heladas", "Activo", "en C4", EstadoSensor.NORMAL),
        Sistema(TipoSistema.ESTANQUE, "Estanque", "72", "%", EstadoSensor.NORMAL, porcentaje = 0.72f),
        Sistema(TipoSistema.CLIMA, "Clima", "4,2", "°C", EstadoSensor.PRECAUCION)
    )

    override fun gruposEquipos(): List<GrupoEquipos> = listOf(
        GrupoEquipos(
            "Electroválvulas", listOf(
                Equipo("Válvula C1", "Cuartel 1", EstadoEquipo.ENCENDIDO),
                Equipo("Válvula C3", "Cuartel 3", EstadoEquipo.FALLA)
            )
        ),
        GrupoEquipos(
            "Bombas de impulsión", listOf(
                Equipo("Bomba 1", "Estanque principal", EstadoEquipo.ENCENDIDO),
                Equipo("Bomba 2", "Estanque principal", EstadoEquipo.APAGADO)
            )
        ),
        GrupoEquipos(
            "Sistema anti-heladas", listOf(
                Equipo("Torre C4", "Cuartel 4", EstadoEquipo.MANTENCION),
                Equipo("Control remoto", "Estación Sur", EstadoEquipo.SIN_RESPUESTA)
            )
        )
    )

    override fun alertas(): List<Alerta> = listOf(
        Alerta(
            id = 1, titulo = "Riesgo de helada", tipo = TipoAlerta.HELADA,
            severidad = Severidad.CRITICA, estado = EstadoGestion.NUEVA,
            cuartelId = 4, ubicacion = "Cuartel 4 · Estación Sur", dia = "Hoy", hora = "05:40",
            descripcion = "Temperatura fuera de rango seguro.",
            avisoTecnico = "Temperatura fuera de rango",
            instruccionTecnico = "Informe a su supervisor y revise el sistema anti-heladas.",
            valorMedido = "1,8", umbral = "3,0", unidad = "°C",
            accion = AccionSugerida(
                titulo = "Verificar sistema anti-heladas",
                detalleSupervisor = "Inspeccionar activación y temperatura del Cuartel 4.",
                detalleTecnico = "Revise el sistema anti-heladas del Cuartel 4."
            ),
            detectadaA = "05:40", notificadaA = "05:41"
        ),
        Alerta(
            id = 2, titulo = "Humedad de suelo baja", tipo = TipoAlerta.HUMEDAD,
            severidad = Severidad.ADVERTENCIA, estado = EstadoGestion.REVISADA,
            cuartelId = 3, ubicacion = "Cuartel 3 · Estación Central", dia = "Hoy", hora = "05:30",
            descripcion = "La humedad del suelo está bajo el mínimo.",
            avisoTecnico = "Humedad fuera de rango",
            instruccionTecnico = "Revise la válvula C3 del Cuartel 3.",
            valorMedido = "31", umbral = "35", unidad = "%",
            accion = AccionSugerida(
                titulo = "Revisar el riego del Cuartel 3",
                detalleSupervisor = "Verificar la electroválvula C3 y la humedad del suelo.",
                detalleTecnico = "Revise la válvula C3 del Cuartel 3."
            ),
            detectadaA = "05:30", notificadaA = "05:31", revisadaPor = "m.soto", revisadaA = "05:52"
        ),
        Alerta(
            id = 3, titulo = "Nivel de estanque recuperado", tipo = TipoAlerta.NIVEL_AGUA,
            severidad = Severidad.INFORMATIVA, estado = EstadoGestion.RESUELTA,
            cuartelId = null, ubicacion = "Estanque principal", dia = "Hoy", hora = "04:21",
            descripcion = "El nivel del estanque volvió al rango normal.",
            avisoTecnico = "Nivel de estanque normal",
            instruccionTecnico = "No se requiere ninguna acción.",
            valorMedido = "72", umbral = "30", unidad = "%",
            accion = null,
            detectadaA = "03:50", notificadaA = "03:51", revisadaPor = "m.soto", revisadaA = "04:05", resueltaA = "04:21"
        ),
        Alerta(
            id = 4, titulo = "Caudal normalizado", tipo = TipoAlerta.FUGA,
            severidad = Severidad.INFORMATIVA, estado = EstadoGestion.RESUELTA,
            cuartelId = 2, ubicacion = "Cuartel 2 · Estación Norte", dia = "Ayer", hora = "18:10",
            descripcion = "El caudal volvió al rango normal.",
            avisoTecnico = "Caudal normal",
            instruccionTecnico = "No se requiere ninguna acción.",
            valorMedido = "14", umbral = "10", unidad = "L/min",
            accion = null,
            detectadaA = "17:32", notificadaA = "17:33", revisadaPor = "c.munoz", revisadaA = "17:50", resueltaA = "18:10"
        )
    )
}
