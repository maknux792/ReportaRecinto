package com.example.reportarecinto.ui.reportes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.viewmodel.ReporteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private fun formatearFecha(fechaMs: Long?): String {
    val valor = fechaMs ?: 0L
    if (valor <= 0L) return "Sin fecha"
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")).apply {
            timeZone = TimeZone.getTimeZone("America/Santiago")
        }
        sdf.format(Date(valor))
    } catch (_: Exception) {
        "Sin fecha"
    }
}

@Composable
fun ListaReportes(
    viewModel: ReporteViewModel,
    correoUsuarioActual: String
) {
    val listaReportes by viewModel.reportes.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    val ticketsDelUsuario = remember(listaReportes, correoUsuarioActual) {
        val correoLimpio = correoUsuarioActual.trim()
        if (correoLimpio.isEmpty()) {
            emptyList()
        } else {
            listaReportes.filter { reporte ->
                reporte.correoUsuario.orEmpty().trim().equals(correoLimpio, ignoreCase = true)
            }
        }
    }

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
            Text("Mis Tickets", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { viewModel.cargarReportes() }) {
                Text("🔄 Actualizar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (cargando && ticketsDelUsuario.isEmpty()) {
            CircularProgressIndicator()
        } else if (ticketsDelUsuario.isEmpty()) {
            Text("No hay reportes registrados aún.")
        } else {
            LazyColumn {
                items(
                    items = ticketsDelUsuario,
                    key = { reporte -> reporte.id.ifBlank { reporte.hashCode().toString() } }
                ) { reporte ->
                    val respuesta = reporte.respuestaAdmin.orEmpty().trim()
                    val estaSolucionado = reporte.estado.orEmpty().equals("Solucionado", ignoreCase = true)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = reporte.titulo.orEmpty(),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("👤 Reportado por: ${reporte.nombreUsuario.orEmpty()}")
                            Text("📍 Lugar: ${reporte.lugar.orEmpty()}")
                            Text("📝 Descripción: ${reporte.descripcion.orEmpty()}")
                            Text("🕒 Creado: ${formatearFecha(reporte.fechaCreacionMs)}")

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Estado: ${reporte.estado.orEmpty()}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (respuesta.isNotEmpty()) {
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
                                            text = respuesta,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            } else if (estaSolucionado) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Solucionado",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}