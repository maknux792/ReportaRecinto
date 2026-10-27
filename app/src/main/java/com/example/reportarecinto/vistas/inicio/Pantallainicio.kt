package com.example.reportarecinto.vistas.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.reportarecinto.R

@Composable
fun PantallaInicio(
    onComenzarClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // IMAGEN DE FONDO (img_1.png)
        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = "Fondo de inicio",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Capa semitransparente para que las letras blancas resalten
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        // Textos y botón de navegación
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bienvenidos a reportEdu",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Donde podrás reportar todos los problemas del colegio de manera rápida y sencilla.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onComenzarClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Comenzar")
            }
        }
    }
}