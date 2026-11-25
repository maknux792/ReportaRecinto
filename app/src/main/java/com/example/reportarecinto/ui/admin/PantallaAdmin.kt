package com.example.reportarecinto.ui.admin

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.reportarecinto.viewmodel.ReporteViewModel

@Composable
fun PantallaAdmin(viewModel: ReporteViewModel) {
    val contexto = LocalContext.current
    val listaReportes by viewModel.reportes.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarReportes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Panel de Administración", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { viewModel.cargarReportes() }) {
                Text("🔄")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (cargando && listaReportes.isEmpty()) {
            CircularProgressIndicator()
        } else if (listaReportes.isEmpty()) {
            Text("No hay reportes registrados.")
        } else {
            LazyColumn {
                items(listaReportes) { reporte ->
                    val msTranscurridos = System.currentTimeMillis() - reporte.fechaCreacionMs
                    val diasAntiguedad = msTranscurridos / (1000 * 60 * 60 * 24)
                    val requiereAlerta10Dias = diasAntiguedad >= 10 && reporte.estado != "Solucionado"
                    val respuestaPrevia = reporte.respuestaAdmin

                    var mensajeInput by remember { mutableStateOf("") }
                    var mostrarImagenAmpliada by remember { mutableStateOf(false) }

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
                            Text(
                                text = "Estado actual: ${reporte.estado}",
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (reporte.fotoUri.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📸 Foto adjunta (toca para ampliar):",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                AsyncImage(
                                    model = reporte.fotoUri,
                                    contentDescription = "Foto adjunta del reporte",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { mostrarImagenAmpliada = true },
                                    contentScale = ContentScale.Crop
                                )
                            }

                            if (mostrarImagenAmpliada && reporte.fotoUri.isNotEmpty()) {
                                AlertDialog(
                                    onDismissRequest = { mostrarImagenAmpliada = false },
                                    confirmButton = {
                                        TextButton(onClick = { mostrarImagenAmpliada = false }) {
                                            Text("Cerrar")
                                        }
                                    },
                                    text = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            AsyncImage(
                                                model = reporte.fotoUri,
                                                contentDescription = "Foto en tamaño completo",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }
                                )
                            }

                            if (respuestaPrevia.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "💬 Último mensaje enviado: $respuestaPrevia",
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
                                Button(
                                    onClick = {
                                        val nuevaRespuesta = if (mensajeInput.isNotBlank()) mensajeInput else respuestaPrevia
                                        val reporteActualizado = reporte.copy(
                                            estado = "En Proceso",
                                            respuestaAdmin = nuevaRespuesta
                                        )
                                        viewModel.actualizarReporte(reporte.id, reporteActualizado) {
                                            mensajeInput = ""
                                            Toast.makeText(contexto, "Estado: En Proceso", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                ) {
                                    Text("En Proceso")
                                }

                                Button(
                                    onClick = {
                                        val nuevaRespuesta = if (mensajeInput.isNotBlank()) mensajeInput else respuestaPrevia
                                        val reporteActualizado = reporte.copy(
                                            estado = "Solucionado",
                                            respuestaAdmin = nuevaRespuesta,
                                            solucion = nuevaRespuesta
                                        )
                                        viewModel.actualizarReporte(reporte.id, reporteActualizado) {
                                            mensajeInput = ""
                                            Toast.makeText(contexto, "Estado: Solucionado", Toast.LENGTH_SHORT).show()
                                        }
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