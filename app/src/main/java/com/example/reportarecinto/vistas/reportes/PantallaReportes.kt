package com.example.reportarecinto.vistas.reportes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.datos.Reporte
import com.example.reportarecinto.logica.GestorReportes

@Composable
fun PantallaReportes() {

    val gestor = GestorReportes()

    var titulo by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Crear reporte")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Lugar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = prioridad,
            onValueChange = { prioridad = it },
            label = { Text("Prioridad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (
                    titulo.isBlank() ||
                    lugar.isBlank() ||
                    descripcion.isBlank() ||
                    prioridad.isBlank()
                ) {
                    mensaje = "Complete todos los campos"
                } else {

                    val nuevoReporte = Reporte(
                        titulo = titulo,
                        lugar = lugar,
                        descripcion = descripcion,
                        prioridad = prioridad
                    )

                    gestor.agregarReporte(nuevoReporte)

                    mensaje = "Reporte guardado correctamente"

                    titulo = ""
                    lugar = ""
                    descripcion = ""
                    prioridad = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar reporte")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(mensaje)
    }
}