package com.example.reportarecinto.datos

import java.util.UUID

data class Reporte(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String = "",
    val nombreUsuario: String = "Anónimo",
    val lugar: String = "",
    val descripcion: String = "",
    val prioridad: String = "",
    val fotoUri: String = "foto_demo.jpg",
    var estado: String = "Recibido",
    var respuestaAdmin: String = "",
    var solucion: String = "",
    val fechaCreacionMs: Long = System.currentTimeMillis()
)