package com.example.reportarecinto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reportarecinto.data.ReporteRepository
import com.example.reportarecinto.model.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReporteViewModel(
    private val repository: ReporteRepository
) : ViewModel() {

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _conectadoApi = MutableStateFlow(true)
    val conectadoApi: StateFlow<Boolean> = _conectadoApi.asStateFlow()

    init {
        cargarReportes()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null
            try {
                val (lista, enLinea) = repository.obtenerReportes()
                _reportes.value = lista
                _conectadoApi.value = enLinea
                if (!enLinea) {
                    _mensajeError.value = "Sin conexión con la API: mostrando datos de SQLite"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error al cargar reportes: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearReporte(nuevoReporte: Reporte, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null
            try {
                val sincronizadoConApi = repository.crearReporte(nuevoReporte)
                _conectadoApi.value = sincronizadoConApi
                val (lista, _) = repository.obtenerReportes()
                _reportes.value = lista
                if (!sincronizadoConApi) {
                    _mensajeError.value = "Guardado en SQLite (se enviará al volver la señal)"
                }
                onExito()
            } catch (e: Exception) {
                _mensajeError.value = "Error al guardar reporte: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizarReporte(id: String, reporteActualizado: Reporte, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null
            try {
                val sincronizadoConApi = repository.actualizarReporte(id, reporteActualizado)
                _conectadoApi.value = sincronizadoConApi
                val (lista, _) = repository.obtenerReportes()
                _reportes.value = lista
                if (!sincronizadoConApi) {
                    _mensajeError.value = "Actualizado en SQLite (pendiente de sincronizar con API)"
                }
                onExito()
            } catch (e: Exception) {
                _mensajeError.value = "Error al actualizar reporte: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }
}