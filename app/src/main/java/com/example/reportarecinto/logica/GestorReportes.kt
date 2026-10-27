package com.example.reportarecinto.logica

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.reportarecinto.datos.Reporte
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GestorReportes(private val context: Context) {

    private val PREFS_NAME = "reportedu_prefs"
    private val KEY_REPORTES = "lista_reportes"
    private val gson = Gson()

    // Lista en memoria sincronizada con Jetpack Compose
    private val _reportes = mutableStateListOf<Reporte>()

    init {
        cargarReportesDesdeAlmacenamiento()
    }

    // Devuelve la lista observable para Compose
    fun obtenerReportes(): SnapshotStateList<Reporte> {
        return _reportes
    }

    // Agrega un reporte y lo guarda localmente
    fun agregarReporte(reporte: Reporte) {
        _reportes.add(0, reporte) // Lo agrega al inicio de la lista
        guardarEnAlmacenamientoLocal()
    }

    // Llama a esta función cada vez que el admin actualiza un reporte (estado o respuesta)
    fun guardarCambios() {
        guardarEnAlmacenamientoLocal()
    }

    // 💾 GUARDA LA LISTA EN EL ALMACENAMIENTO INTERNO (SharedPreferences)
    private fun guardarEnAlmacenamientoLocal() {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(_reportes.toList())
        sharedPreferences.edit().putString(KEY_REPORTES, json).apply()
    }

    // 📂 CARGA LOS DATOS AL INICIAR LA APP
    private fun cargarReportesDesdeAlmacenamiento() {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(KEY_REPORTES, null)

        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<List<Reporte>>() {}.type
            val reportesGuardados: List<Reporte> = gson.fromJson(json, type)
            _reportes.clear()
            _reportes.addAll(reportesGuardados)
        }
    }
}