package com.example.mpazpanol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpazpanol.ui.components.CampoFormulario
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.viewmodel.HistorialViewModel

@Composable
fun Historial(
    viewModel: HistorialViewModel = viewModel()
) {
    val context = LocalContext.current
    val devolucion = viewModel.devolucion

    Scaffold(
        topBar = { Header(title = "HISTORIAL") },
        bottomBar = { Footer() }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Consultar devolución",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ingresa el número del registro que deseas consultar.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoFormulario(
                value = viewModel.numero,
                onValueChange = {
                    viewModel.onNumeroChanged(it)
                },
                label = "Número de devolución",
                icono = Icons.Default.Info,
                error = viewModel.errorNumero,
                placeholder = "Ej: 1",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.buscarDevolucion(context)
                },
                enabled = !viewModel.buscando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.buscando) {
                    Text("Buscando...")
                } else {
                    Text("Buscar devolución")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (devolucion != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Devolución N.º ${devolucion.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Material: ${devolucion.material}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Cantidad: ${devolucion.cantidad}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Sala: ${devolucion.sala}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Estado: ${devolucion.estado}")

                        Spacer(modifier = Modifier.height(8.dp))

                        if (devolucion.observaciones.isBlank()) {
                            Text("Observaciones: sin observaciones.")
                        } else {
                            Text(
                                "Observaciones: ${devolucion.observaciones}"
                            )
                        }
                    }
                }
            }
        }
    }
}