package com.example.reportarecinto.vistas.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PantallaLogin(
    onLoginExitoso: (esAdmin: Boolean) -> Unit
) {
    val contexto = LocalContext.current
    var esModoRegistro by remember { mutableStateOf(false) }
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (esModoRegistro) "Registrar Usuario" else "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Correo o Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (usuario.isBlank() || clave.isBlank()) {
                    Toast.makeText(contexto, "Complete los datos", Toast.LENGTH_SHORT).show()
                } else {
                    val esAdmin = usuario.contains("admin", ignoreCase = true)
                    Toast.makeText(
                        contexto,
                        if (esModoRegistro) "Usuario registrado correctamente" else "Bienvenido",
                        Toast.LENGTH_SHORT
                    ).show()
                    onLoginExitoso(esAdmin)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (esModoRegistro) "Registrarme e Ingresar" else "Ingresar")
        }

        TextButton(onClick = { esModoRegistro = !esModoRegistro }) {
            Text(if (esModoRegistro) "¿Ya tienes cuenta? Inicia Sesión" else "¿No tienes cuenta? Regístrate aquí")
        }
    }
}