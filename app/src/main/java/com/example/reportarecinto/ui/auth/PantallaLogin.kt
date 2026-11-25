package com.example.reportarecinto.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.data.RetrofitClient
import com.example.reportarecinto.model.Usuario
import kotlinx.coroutines.launch

@Composable
fun PantallaLogin(
    onLoginExitoso: (esAdmin: Boolean, correoUsuario: String) -> Unit
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()

    var esModoRegistro by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var enviando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (esModoRegistro) "Registrar Usuario" else "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (esModoRegistro) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Correo") },
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
            enabled = !enviando,
            onClick = {
                val correoIngresado = usuario.trim()
                if (correoIngresado.isBlank() || clave.isBlank() || (esModoRegistro && nombre.isBlank())) {
                    Toast.makeText(contexto, "Complete los datos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                enviando = true
                scope.launch {
                    try {
                        if (esModoRegistro) {
                            val rolAsignado = if (correoIngresado.contains("admin", ignoreCase = true)) "ADMIN" else "USUARIO"
                            val nuevoUsuario = Usuario(
                                nombre = nombre.trim(),
                                correo = correoIngresado,
                                password = clave,
                                rol = rolAsignado
                            )
                            val resp = RetrofitClient.usuarioApi.registrarUsuario(nuevoUsuario)
                            if (resp.isSuccessful) {
                                val registrado = resp.body()
                                val correoFinal = registrado?.correo?.takeIf { it.isNotBlank() } ?: correoIngresado
                                Toast.makeText(contexto, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                                onLoginExitoso(
                                    registrado?.rol.equals("ADMIN", ignoreCase = true),
                                    correoFinal
                                )
                            } else {
                                Toast.makeText(contexto, "No se pudo registrar (código ${resp.code()})", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val resp = RetrofitClient.usuarioApi.login(
                                mapOf("correo" to correoIngresado, "password" to clave)
                            )
                            if (resp.isSuccessful) {
                                val user = resp.body()
                                val correoFinal = user?.correo?.takeIf { it.isNotBlank() } ?: correoIngresado
                                Toast.makeText(contexto, "Bienvenido ${user?.nombre.orEmpty()}", Toast.LENGTH_SHORT).show()
                                onLoginExitoso(
                                    user?.rol.equals("ADMIN", ignoreCase = true),
                                    correoFinal
                                )
                            } else {
                                Toast.makeText(contexto, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (_: Exception) {
                        // Fallback si no hay conexión con la API en este momento
                        val esAdmin = correoIngresado.contains("admin", ignoreCase = true)
                        Toast.makeText(contexto, "Sin conexión a API: acceso en modo local", Toast.LENGTH_SHORT).show()
                        onLoginExitoso(esAdmin, correoIngresado)
                    } finally {
                        enviando = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                when {
                    enviando -> "Conectando..."
                    esModoRegistro -> "Registrarme e Ingresar"
                    else -> "Ingresar"
                }
            )
        }

        TextButton(onClick = { esModoRegistro = !esModoRegistro }) {
            Text(if (esModoRegistro) "¿Ya tienes cuenta? Inicia Sesión" else "¿No tienes cuenta? Regístrate aquí")
        }
    }
}