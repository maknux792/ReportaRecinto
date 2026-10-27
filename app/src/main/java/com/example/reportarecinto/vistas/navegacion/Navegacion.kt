package com.example.reportarecinto.vistas.navegacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.logica.GestorReportes
import com.example.reportarecinto.vistas.reportes.ListaReportes
import com.example.reportarecinto.vistas.reportes.PantallaReportes

@Composable
fun Navegacion(
    gestor: GestorReportes
) {

    var pantallaActual by remember {
        mutableStateOf("crear")
    }

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Button(
                onClick = {
                    pantallaActual = "crear"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
            ) {
                Text("Crear Ticket")
            }

            Button(
                onClick = {
                    pantallaActual = "lista"
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp)
            ) {
                Text("Mis Tickets")
            }
        }

        when (pantallaActual) {

            "crear" -> {
                PantallaReportes(
                    gestor = gestor
                )
            }

            "lista" -> {
                ListaReportes(
                    gestor = gestor
                )
            }
        }
    }
}