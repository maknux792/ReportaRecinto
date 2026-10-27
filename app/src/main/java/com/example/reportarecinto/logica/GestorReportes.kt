package com.example.reportarecinto.logica

import androidx.compose.runtime.mutableStateListOf
import com.example.reportarecinto.datos.Reporte

class GestorReportes {

    val listaReportes = mutableStateListOf<Reporte>()

    // Lista de recintos del colegio ordenados por prioridad física
    val recintosColegio = listOf(
        "Salas de clases",
        "Pasillos",
        "Cancha",
        "Biblioteca",
        "Salas de computación",
        "Centro de innovación escolar",
        "Casinos",
        "Sala de profesores",
        "Sala PIE",
        "Sala sensorial",
        "Enfermería",
        "Invernadero"
    )

    fun agregarReporte(reporte: Reporte) {
        listaReportes.add(0, reporte)
    }

    fun obtenerReportes(): List<Reporte> {
        return listaReportes
    }

    fun responderTicket(id: String, nuevoEstado: String, respuesta: String) {
        val index = listaReportes.indexOfFirst { it.id == id }
        if (index != -1) {
            val actual = listaReportes[index]
            actual.estado = nuevoEstado
            actual.respuestaAdmin = respuesta
            listaReportes[index] = actual.copy() // Forzar recomposición en UI
        }
    }
}