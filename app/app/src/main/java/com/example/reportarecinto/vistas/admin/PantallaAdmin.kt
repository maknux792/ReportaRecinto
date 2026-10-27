package com.example.reportarecinto.vistas.admin

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.logica.GestorReportes

@Composable
fun PantallaAdmin(gestor: GestorReportes) {
    val reportes = gestor.obtenerReportes()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Panel de Administración", style = MaterialTheme.typography.headlineMedium)
        Text("Gestión y Solución de Tickets", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (reportes.isEmpty()) {
            Text("No hay tickets pendientes.")
        } else {
            LazyColumn {
                items(reportes) { reporte ->
                    var respuestaText by remember { mutableStateOf(reporte.respuestaAdmin) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .animateContentSize() // Animación de interfaz exigida en la nota 2
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Ticket: ${reporte.titulo}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Reportado por: ${reporte.nombreUsuario}")
                            Text(text = "Lugar: ${reporte.lugar} | Prioridad: ${reporte.prioridad}")
                            Text(text = "Estado actual: ${reporte.estado}", color = Color(0xFFD32F2F))
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = respuestaText,
                                onValueChange = { respuestaText = it },
                                label = { Text("Respuesta / Feedback al emisor") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Button(onClick = { gestor.responderTicket(reporte.id, "En proceso", respuestaText) }) {
                                    Text("En Proceso")
                                }
                                Button(
                                    onClick = { gestor.responderTicket(reporte.id, "Solucionado", respuestaText) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                                ) {
                                    Text("Solucionado")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}