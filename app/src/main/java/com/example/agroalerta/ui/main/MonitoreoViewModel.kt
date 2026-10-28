package com.example.agroalerta.ui.main

import androidx.lifecycle.ViewModel
import com.example.agroalerta.data.model.Alerta
import com.example.agroalerta.data.model.Cuartel
import com.example.agroalerta.data.model.EstadoGestion
import com.example.agroalerta.data.model.GrupoEquipos
import com.example.agroalerta.data.model.Sistema
import com.example.agroalerta.data.repository.MonitoreoRepository
import com.example.agroalerta.data.repository.MonitoreoRepositoryFalso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel compartido por todas las pantallas de monitoreo.
 * Hoy lee de [MonitoreoRepositoryFalso]; en la próxima entrega se reemplaza por la API (Retrofit).
 */
class MonitoreoViewModel : ViewModel() {

    private val repositorio: MonitoreoRepository = MonitoreoRepositoryFalso()

    val cuarteles: List<Cuartel> = repositorio.cuarteles()
    val sistemas: List<Sistema> = repositorio.sistemas()
    val gruposEquipos: List<GrupoEquipos> = repositorio.gruposEquipos()

    private val _alertas = MutableStateFlow(repositorio.alertas())
    val alertas: StateFlow<List<Alerta>> = _alertas.asStateFlow()

    /** Simula la conectividad intermitente: muestra el banner de "últimas lecturas". */
    private val _datosDesactualizados = MutableStateFlow(false)
    val datosDesactualizados: StateFlow<Boolean> = _datosDesactualizados.asStateFlow()

    fun alternarDatosDesactualizados() {
        _datosDesactualizados.update { !it }
    }

    /** Solo el supervisor llega a esta acción desde la UI. Deja registro de quién y cuándo. */
    fun marcarRevisada(idAlerta: Int, usuario: String) {
        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        _alertas.update { lista ->
            lista.map { alerta ->
                if (alerta.id == idAlerta && alerta.estado == EstadoGestion.NUEVA) {
                    alerta.copy(estado = EstadoGestion.REVISADA, revisadaPor = usuario, revisadaA = hora)
                } else alerta
            }
        }
    }
}
