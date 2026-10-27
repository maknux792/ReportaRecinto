package com.example.reportarecinto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.reportarecinto.logica.GestorReportes
import com.example.reportarecinto.ui.theme.ReportaRecintoTheme
import com.example.reportarecinto.vistas.admin.PantallaAdmin
import com.example.reportarecinto.vistas.auth.PantallaLogin
import com.example.reportarecinto.vistas.inicio.PantallaInicio
import com.example.reportarecinto.vistas.reportes.ListaReportes
import com.example.reportarecinto.vistas.reportes.PantallaReportes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gestor = GestorReportes()

        setContent {
            ReportaRecintoTheme {
                // Control general del flujo inicial
                var enBienvenida by remember { mutableStateOf(true) }
                var sesionIniciada by remember { mutableStateOf(false) }

                // Control de pestañas de navegación (0: Mis Tickets, 1: Crear Ticket, 2: Admin)
                var pantallaActual by remember { mutableIntStateOf(0) }

                when {
                    // 1. PRIMERA PANTALLA: Bienvenida reportEdu
                    enBienvenida -> {
                        PantallaInicio(
                            onComenzarClick = {
                                enBienvenida = false // Pasa al Login
                            }
                        )
                    }

                    // 2. SEGUNDA PANTALLA: Login
                    !sesionIniciada -> {
                        PantallaLogin(
                            onLoginExitoso = { esAdmin ->
                                sesionIniciada = true
                                pantallaActual = if (esAdmin) 2 else 0 // Si es admin va a Admin, si no a Mis Tickets
                            }
                        )
                    }

                    // 3. TERCERA ETAPA: Aplicación con Barra de Navegación Inferior
                    else -> {
                        Scaffold(
                            bottomBar = {
                                NavigationBar {
                                    NavigationBarItem(
                                        icon = { Text("📋") },
                                        label = { Text("Mis Tickets") },
                                        selected = pantallaActual == 0,
                                        onClick = { pantallaActual = 0 }
                                    )
                                    NavigationBarItem(
                                        icon = { Text("➕") },
                                        label = { Text("Crear Ticket") },
                                        selected = pantallaActual == 1,
                                        onClick = { pantallaActual = 1 }
                                    )
                                    NavigationBarItem(
                                        icon = { Text("⚙️") },
                                        label = { Text("Admin") },
                                        selected = pantallaActual == 2,
                                        onClick = { pantallaActual = 2 }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            Surface(modifier = Modifier.padding(innerPadding)) {
                                when (pantallaActual) {
                                    0 -> ListaReportes(gestor = gestor)
                                    1 -> PantallaReportes(
                                        gestor = gestor,
                                        onReporteGuardado = { pantallaActual = 0 }
                                    )
                                    2 -> PantallaAdmin(gestor = gestor)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}