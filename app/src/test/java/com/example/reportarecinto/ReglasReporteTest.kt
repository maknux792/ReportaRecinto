package com.example.reportarecinto

import com.example.reportarecinto.model.ReglasReporte
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasReporteTest {

    @Test
    fun calcularPrioridadPorLugar_retornaAltaEnSalasCriticas() {
        val resultado = ReglasReporte.calcularPrioridadPorLugar("Enfermería")
        assertEquals("Alta", resultado)
    }

    @Test
    fun esFormularioReporteValido_retornaTrueCuandoTodosLosCamposEstanCompletos() {
        val valido = ReglasReporte.esFormularioReporteValido(
            titulo = "Fuga de agua",
            nombreUsuario = "Ana",
            lugar = "Casino",
            descripcion = "Gotera en el techo",
            tieneFoto = true
        )
        assertTrue(valido)
    }

    @Test
    fun esFormularioReporteValido_retornaFalseCuandoFaltaFoto() {
        val valido = ReglasReporte.esFormularioReporteValido(
            titulo = "Fuga de agua",
            nombreUsuario = "Ana",
            lugar = "Casino",
            descripcion = "Gotera en el techo",
            tieneFoto = false
        )
        assertFalse(valido)
    }

    @Test
    fun esRolAdmin_retornaTrueParaRolAdmin() {
        assertTrue(ReglasReporte.esRolAdmin("ADMIN"))
    }
}