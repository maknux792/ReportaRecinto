package com.example.reportarecinto.vistas.admin

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.logica.GestorReportes

@Composable
fun PantallaAdmin(gestor: GestorReportes) {
    val contexto = LocalContext.current
    val listaReportes = gestor.obtenerReportes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Panel de Administración", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (listaReportes.isEmpty()) {
            Text("No hay reportes registrados.")
        } else {
            LazyColumn {
                items(listaReportes) { reporte ->
                    val msTranscurridos = System.currentTimeMillis() - reporte.fechaCreacionMs
                    val diasAntiguedad = msTranscurridos / (1000 * 60 * 60 * 24)
                    val requiereAlerta10Dias = diasAntiguedad >= 10 && reporte.estado != "Solucionado"

                    // Inicializamos en blanco para que no vuelva a cargarse al cambiar de vista
                    var mensajeInput by remember { mutableStateOf("") }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .animateContentSize()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (requiereAlerta10Dias) {
                                Surface(
                                    color = Color(0xFFD32F2F),
                                    shape = MaterialTheme.shapes.extraSmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                ) {
                                    Text(
                                        text = "⚠️ RECORDATORIO: Lleva más de 10 días sin resolverse",
                                        color = Color.White,
                                        modifier = Modifier.padding(6.dp),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Text(reporte.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("👤 Usuario: ${reporte.nombreUsuario}")
                            Text("📍 Lugar: ${reporte.lugar}")

                            Text(
                                text = "⚡ Prioridad: ${reporte.prioridad}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text("Estado actual: ${reporte.estado}", color = MaterialTheme.colorScheme.primary)

                            // Muestra el último mensaje enviado previamente por el admin (si existe)
                            if (reporte.respuestaAdmin.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "💬 Último mensaje enviado: ${reporte.respuestaAdmin}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = mensajeInput,
                                onValueChange = { mensajeInput = it },
                                label = { Text("Escribir nuevo mensaje/respuesta") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
// Dentro del botón "En Proceso":
                                Button(
                                    onClick = {
                                        reporte.estado = "En Proceso"
                                        if (mensajeInput.isNotBlank()) {
                                            reporte.respuestaAdmin = mensajeInput
                                        }
                                        mensajeInput = ""
                                        gestor.guardarCambios() // <--- GUARDA EL CAMBIO LOCALMENTE
                                        Toast.makeText(contexto, "Estado: En Proceso", Toast.LENGTH_SHORT).show()
                                    }
                                ) {
                                    Text("En Proceso")
                                }

// Dentro del botón "Solucionado":
                                Button(
                                    onClick = {
                                        reporte.estado = "Solucionado"
                                        if (mensajeInput.isNotBlank()) {
                                            reporte.respuestaAdmin = mensajeInput
                                        }
                                        mensajeInput = ""
                                        gestor.guardarCambios() // <--- GUARDA EL CAMBIO LOCALMENTE
                                        Toast.makeText(contexto, "Estado: Solucionado", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
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
