package com.example.reportarecinto.logica

import com.example.reportarecinto.datos.Reporte

class GestorReportes {

    private val listaReportes = mutableListOf<Reporte>()

    fun agregarReporte(reporte: Reporte) {
        listaReportes.add(reporte)
    }

    fun obtenerReportes(): List<Reporte> {
        return listaReportes
    }

    fun eliminarReporte(reporte: Reporte) {
        listaReportes.remove(reporte)
    }
}