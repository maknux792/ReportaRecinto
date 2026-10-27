package com.example.reportarecinto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.reportarecinto.logica.GestorReportes
import com.example.reportarecinto.ui.theme.ReportaRecintoTheme
import com.example.reportarecinto.vistas.reportes.PantallaReportes

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gestor = GestorReportes()

        setContent {
            ReportaRecintoTheme {
                PantallaReportes(
                    gestor = gestor
                )
            }
        }
    }
}