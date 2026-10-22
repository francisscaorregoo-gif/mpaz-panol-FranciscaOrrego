package com.example.mpazpanol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.components.Footer

@Composable
fun Inventario() {

    Scaffold(
        topBar = { Header(title = "INVENTARIO") },
        bottomBar = { Footer() }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Materiales del pañol",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cantidades iniciales de referencia",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Proyector: 3 unidades")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Notebook: 5 unidades")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Cable HDMI: 8 unidades")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Parlante: 4 unidades")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Control remoto: 6 unidades")

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}