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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.datos.Reporte
import com.example.reportarecinto.logica.GestorReportes

@Composable
fun PantallaReportes(
    gestor: GestorReportes,
    onReporteGuardado: () -> Unit = {}
) {
    val contexto = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }
    var uriImagenSeleccionada by remember { mutableStateOf<Uri?>(null) }

    // Launcher nativo para seleccionar una imagen desde el almacenamiento/galería del dispositivo
    val selectorImagenLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            uriImagenSeleccionada = uri
            Toast.makeText(contexto, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Crear reporte",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre de quien registra") },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Lugar") },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = prioridad,
            onValueChange = { prioridad = it },
            label = { Text("Prioridad") },
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Botón con selector de imagen real nativo de Android
        OutlinedButton(
            onClick = {
                // Abre el selector de archivos del sistema buscando imágenes
                selectorImagenLauncher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uriImagenSeleccionada == null)
                    "📸 Seleccionar/Adjuntar Foto"
                else
                    "✓ Foto Seleccionada"
            )
        }

        if (uriImagenSeleccionada != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Archivo: ${uriImagenSeleccionada?.lastPathSegment ?: "Imagen cargada"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (
                    titulo.isBlank() ||
                    lugar.isBlank() ||
                    descripcion.isBlank() ||
                    nombreUsuario.isBlank()
                ) {
                    Toast.makeText(
                        contexto,
                        "Complete todos los campos obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (uriImagenSeleccionada == null) {
                    Toast.makeText(
                        contexto,
                        "Por favor adjunte una fotografía del problema",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val nuevoReporte = Reporte(
                        titulo = titulo,
                        nombreUsuario = nombreUsuario,
                        lugar = lugar,
                        descripcion = descripcion,
                        prioridad = prioridad,
                        fotoUri = uriImagenSeleccionada.toString(),
                        estado = "Recibido",
                        solucion = ""
                    )

                    gestor.agregarReporte(nuevoReporte)

                    Toast.makeText(
                        contexto,
                        "Reporte guardado correctamente con imagen",
                        Toast.LENGTH_SHORT
                    ).show()

                    titulo = ""
                    lugar = ""
                    descripcion = ""
                    prioridad = ""
                    nombreUsuario = ""
                    uriImagenSeleccionada = null
                    onReporteGuardado()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar reporte")
        }
    }
}