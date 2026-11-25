package com.example.reportarecinto.data

import android.util.Log
import com.example.reportarecinto.model.Reporte
import java.util.UUID

class ReporteRepository(
    private val dbHelper: ReporteDatabaseHelper,
    private val api: ReporteApiService = RetrofitClient.api
) {

    companion object {
        private const val TAG = "ReporteRepoDebug"
    }

    suspend fun sincronizarPendientes(): Boolean {
        return try {
            val pendientes = dbHelper.obtenerPendientesDeSincronizar()
            for (pendiente in pendientes) {
                val creado = api.crearReporte(pendiente)
                dbHelper.insertarOActualizarReporte(creado, sincronizado = true)
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error sincronizando pendientes: ${e.message}", e)
            false
        }
    }

    suspend fun obtenerReportes(): Pair<List<Reporte>, Boolean> {
        return try {
            sincronizarPendientes()
            val remotos = api.obtenerReportes().reversed()
            remotos.forEach { reporte ->
                dbHelper.insertarOActualizarReporte(reporte, sincronizado = true)
            }
            Pair(remotos, true) // true = conectado a API
        } catch (e: Exception) {
            Log.e(TAG, "Error obtenerReportes -> usando SQLite: ${e.message}", e)
            val locales = dbHelper.obtenerTodosLosReportes()
            Pair(locales, false) // false = usando SQLite local
        }
    }

    suspend fun crearReporte(reporte: Reporte): Boolean {
        val reporteConId = if (reporte.id.isBlank()) {
            reporte.copy(
                id = UUID.randomUUID().toString(),
                fechaCreacionMs = if (reporte.fechaCreacionMs == 0L) System.currentTimeMillis() else reporte.fechaCreacionMs
            )
        } else {
            reporte
        }

        // 1. Guarda en SQLite como no sincronizado
        dbHelper.insertarOActualizarReporte(reporteConId, sincronizado = false)

        // 2. Intenta enviar a la API
        return try {
            val guardadoRemoto = api.crearReporte(reporteConId)
            dbHelper.insertarOActualizarReporte(guardadoRemoto, sincronizado = true)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error crearReporte -> queda en SQLite: ${e.message}", e)
            false
        }
    }

    suspend fun actualizarReporte(id: String, reporteActualizado: Reporte): Boolean {
        // 1. Actualiza en SQLite
        dbHelper.insertarOActualizarReporte(reporteActualizado, sincronizado = false)

        // 2. Intenta actualizar en la API
        return try {
            val respuesta = api.actualizarReporte(id, reporteActualizado)
            if (respuesta.isSuccessful) {
                val cuerpo = respuesta.body() ?: reporteActualizado
                dbHelper.insertarOActualizarReporte(cuerpo, sincronizado = true)
                true
            } else {
                Log.e(TAG, "Error HTTP actualizarReporte: ${respuesta.code()} ${respuesta.message()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizarReporte -> queda en SQLite: ${e.message}", e)
            false
        }
    }
}