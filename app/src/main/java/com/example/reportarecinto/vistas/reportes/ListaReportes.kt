package com.example.reportarecinto.vistas.reportes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.logica.GestorReportes

@Composable
fun ListaReportes(
    gestor: GestorReportes
) {

    val reportes = gestor.obtenerReportes()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Mis Tickets")

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (reportes.isEmpty()) {

            Text("No hay tickets registrados.")

        } else {

            for (reporte in reportes) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = reporte.titulo
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = "Lugar: ${reporte.lugar}"
                        )

                        Text(
                            text = "Prioridad: ${reporte.prioridad}"
                        )

                        Text(
                            text = "Estado: ${reporte.estado}"
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = reporte.descripcion
                        )
                    }
                }
            }
        }
    }
}