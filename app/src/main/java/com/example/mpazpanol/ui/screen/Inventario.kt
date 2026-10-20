
package com.example.mpazpanol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.components.Footer
import androidx.compose.ui.tooling.preview.Preview

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
                text = "Materiales disponibles",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Proyector - Disponibles: 3")
            Spacer(modifier = Modifier.height(24.dp))
            Text("Notebook - Disponibles: 5")
            Spacer(modifier = Modifier.height(24.dp))
            Text("Cable HDMI - Disponibles: 8")
            Spacer(modifier = Modifier.height(24.dp))
            Text("Parlante - Disponibles: 4")
            Spacer(modifier = Modifier.height(24.dp))
            Text("Control remoto - Disponibles: 6")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun InventarioPreview() {
    Inventario()
}