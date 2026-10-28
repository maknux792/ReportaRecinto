package com.example.reportarecinto

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.logica.GestorReportes
import com.example.reportarecinto.ui.theme.ReportaRecintoTheme
import com.example.reportarecinto.vistas.admin.PantallaAdmin
import com.example.reportarecinto.vistas.auth.PantallaLogin
import com.example.reportarecinto.vistas.inicio.PantallaInicio
import com.example.reportarecinto.vistas.reportes.ListaReportes
import com.example.reportarecinto.vistas.reportes.PantallaReportes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gestor = GestorReportes(applicationContext)

        setContent {
            ReportaRecintoTheme {
                var enBienvenida by remember { mutableStateOf(true) }
                var sesionIniciada by remember { mutableStateOf(false) }
                var pantallaActual by remember { mutableIntStateOf(0) }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                when {
                    // 1. BIENVENIDA
                    enBienvenida -> {
                        PantallaInicio(onComenzarClick = { enBienvenida = false })
                    }

                    // 2. INICIO DE SESIÓN
                    !sesionIniciada -> {
                        PantallaLogin(
                            onLoginExitoso = { esAdmin ->
                                sesionIniciada = true
                                pantallaActual = if (esAdmin) 2 else 0
                            }
                        )
                    }

                    // 3. APLICACIÓN PRINCIPAL
                    else -> {
                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "reportEdu - Menú",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                    HorizontalDivider()
                                    NavigationDrawerItem(
                                        label = { Text("Mis Tickets") },
                                        selected = pantallaActual == 0,
                                        onClick = {
                                            pantallaActual = 0
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                    NavigationDrawerItem(
                                        label = { Text("Crear Ticket") },
                                        selected = pantallaActual == 1,
                                        onClick = {
                                            pantallaActual = 1
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                    NavigationDrawerItem(
                                        label = { Text("Panel Admin") },
                                        selected = pantallaActual == 2,
                                        onClick = {
                                            pantallaActual = 2
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                    Spacer(modifier = Modifier.weight(1f))

                                    // CERRAR SESIÓN DESDE EL MENÚ LATERAL
                                    NavigationDrawerItem(
                                        label = { Text("🚪 Cerrar Sesión") },
                                        selected = false,
                                        onClick = {
                                            gestor.cerrarSesionYBorrarTodo()
                                            Toast.makeText(applicationContext, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                                            sesionIniciada = false
                                            enBienvenida = true
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        ) {
                            Scaffold(
                                topBar = {
                                    TopAppBar(
                                        title = { Text("reportEdu") },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                scope.launch { drawerState.open() }
                                            }) {
                                                Text("☰", style = MaterialTheme.typography.headlineMedium)
                                            }
                                        },
                                        actions = {
                                            // CERRAR SESIÓN DESDE LA BARRA SUPERIOR
                                            TextButton(onClick = {
                                                gestor.cerrarSesionYBorrarTodo()
                                                Toast.makeText(applicationContext, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                                                sesionIniciada = false
                                                enBienvenida = true
                                            }) {
                                                Text("Salir")
                                            }
                                        }
                                    )
                                },
                                bottomBar = {
                                    NavigationBar {
                                        NavigationBarItem(
                                            icon = { Text("📋") },
                                            label = { Text("Tickets") },
                                            selected = pantallaActual == 0,
                                            onClick = { pantallaActual = 0 }
                                        )
                                        NavigationBarItem(
                                            icon = { Text("➕") },
                                            label = { Text("Crear") },
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
}