package com.example.mpazpanol.ui.screen

import androidx.compose.ui.platform.LocalContext
import com.example.mpazpanol.ui.components.TarjetaBateria
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header

@Composable
fun Principal(
    onInventario: () -> Unit = {},
    onPrestamos: () -> Unit = {},
    onDevoluciones: () -> Unit = {},
    onHistorial: () -> Unit = {},
) {
    val context = LocalContext.current
    Scaffold (
        topBar = { Header(title = "MÉNÚ PRINCIPAL") },
        bottomBar = { Footer() }
    ) { innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "¿Donde desea ingresar?",
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            TarjetaBateria(context)

            Spacer(modifier = Modifier.height(24.dp))

            Button (
                onClick = onInventario,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4))
            ) {
                Text("Inventario")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onPrestamos,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00BCD4))
            ) {
                Text("Préstamos")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDevoluciones,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4))
            ) {
                Text("Devoluciones")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onHistorial,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4))
            ) {
                Text("Historial")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrincipalPreview() {
    Principal()
}