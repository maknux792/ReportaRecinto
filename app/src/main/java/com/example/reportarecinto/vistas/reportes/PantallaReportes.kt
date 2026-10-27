package com.example.reportarecinto.vistas.reportes
//Muestra el formulario para crear un nuevo ticket
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.graphics.Color
@Composable
fun PantallaReportes(
    gestor: GestorReportes
) {

    val contexto = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("") }

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
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black
            ), modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Lugar") },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black
            ), modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = prioridad,
            onValueChange = { prioridad = it },
            label = { Text("Prioridad") },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black
            ), modifier = Modifier.fillMaxWidth()
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

                    Toast.makeText(
                        contexto,
                        "Complete todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    val nuevoReporte = Reporte(
                        titulo = titulo,
                        lugar = lugar,
                        descripcion = descripcion,
                        prioridad = prioridad,
                        estado = "Pendiente",
                        solucion = ""
                    )

                    gestor.agregarReporte(nuevoReporte)

                    Toast.makeText(
                        contexto,
                        "Reporte guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

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
    }
}
