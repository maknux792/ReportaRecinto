package com.example.reportarecinto.model

object ReglasReporte {

    fun calcularPrioridadPorLugar(lugar: String): String {
        return when (lugar) {
            "Enfermería", "Sala PIE", "Sala Sensorial", "Salas de Clases", "Pasillos" -> "Alta"
            "Salas de Computación", "Centro de Innovación Escolar", "Casino" -> "Media"
            else -> "Baja"
        }
    }

    fun esFormularioReporteValido(
        titulo: String,
        nombreUsuario: String,
        lugar: String,
        descripcion: String,
        tieneFoto: Boolean
    ): Boolean {
        return titulo.isNotBlank() &&
                nombreUsuario.isNotBlank() &&
                lugar.isNotBlank() &&
                descripcion.isNotBlank() &&
                tieneFoto
    }

    fun esRolAdmin(rol: String?): Boolean {
        return rol.equals("ADMIN", ignoreCase = true)
    }
}