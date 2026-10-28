package com.example.reportarecinto.vistas.reportes

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.datos.Reporte
import com.example.reportarecinto.logica.GestorReportes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReportes(
    gestor: GestorReportes,
    onReporteGuardado: () -> Unit = {}
) {
    val contexto = LocalContext.current

    val lugaresDisponibles = listOf(
        "Enfermería", "Sala PIE", "Sala Sensorial", "Salas de Clases", "Pasillos",
        "Salas de Computación", "Centro de Innovación Escolar", "Casino",
        "Biblioteca", "Sala de Profesores", "Invernadero", "Cancha"
    )

    fun calcularPrioridad(lugar: String): String {
        return when (lugar) {
            "Enfermería", "Sala PIE", "Sala Sensorial", "Salas de Clases", "Pasillos" -> "Alta"
            "Salas de Computación", "Centro de Innovación Escolar", "Casino" -> "Media"
            else -> "Baja"
        }
    }

    var titulo by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var lugarSeleccionado by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var uriImagenSeleccionada by remember { mutableStateOf<Uri?>(null) }
    var menuLugarExpandido by remember { mutableStateOf(false) }

    val selectorImagenLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            uriImagenSeleccionada = uri
            Toast.makeText(contexto, "Imagen adjuntada", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Crear reporte", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título del reporte") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre de quien registra") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = menuLugarExpandido,
            onExpandedChange = { menuLugarExpandido = !menuLugarExpandido }
        ) {
            OutlinedTextField(
                value = if (lugarSeleccionado.isEmpty()) "Seleccione un lugar..." else lugarSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Lugar del recinto") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuLugarExpandido) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = menuLugarExpandido,
                onDismissRequest = { menuLugarExpandido = false }
            ) {
                lugaresDisponibles.forEach { lugar ->
                    DropdownMenuItem(
                        text = { Text(lugar) },
                        onClick = {
                            lugarSeleccionado = lugar
                            menuLugarExpandido = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción de la problemática") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = { selectorImagenLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uriImagenSeleccionada == null) "📸 Adjuntar Foto" else "✓ Foto Adjuntada")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (titulo.isBlank() || nombreUsuario.isBlank() || descripcion.isBlank() || lugarSeleccionado.isBlank()) {
                    Toast.makeText(contexto, "Complete todos los campos y seleccione un lugar", Toast.LENGTH_SHORT).show()
                } else if (uriImagenSeleccionada == null) {
                    Toast.makeText(contexto, "Debe adjuntar una foto", Toast.LENGTH_SHORT).show()
                } else {
                    val prioridadOculta = calcularPrioridad(lugarSeleccionado)
                    gestor.agregarReporte(
                        Reporte(
                            titulo = titulo,
                            nombreUsuario = nombreUsuario,
                            lugar = lugarSeleccionado,
                            descripcion = descripcion,
                            prioridad = prioridadOculta,
                            fotoUri = uriImagenSeleccionada.toString(),
                            estado = "Recibido"
                        )
                    )
                    Toast.makeText(contexto, "Reporte enviado correctamente", Toast.LENGTH_SHORT).show()
                    onReporteGuardado()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar reporte")
        }
    }
}