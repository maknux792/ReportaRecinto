package com.example.reportarecinto.vistas.reportes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.logica.GestorReportes

@Composable
fun ListaReportes(gestor: GestorReportes) {
    val listaReportes = gestor.obtenerReportes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis Tickets", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        if (listaReportes.isEmpty()) {
            Text("No hay reportes registrados aún.")
        } else {
            LazyColumn {
                items(listaReportes) { reporte ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = reporte.titulo,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("👤 Reportado por: ${reporte.nombreUsuario}")
                            Text("📍 Lugar: ${reporte.lugar}")
                            Text("📝 Descripción: ${reporte.descripcion}")

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Estado: ${reporte.estado}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Muestra la respuesta enviada por el administrador si existe
                            if (reporte.respuestaAdmin.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.small,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "💬 Respuesta del Administrador:",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Text(
                                            text = reporte.respuestaAdmin,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}