package com.example.mpazpanol.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
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
import com.example.mpazpanol.ui.components.vibrar
import com.example.mpazpanol.ui.viewmodel.DevolucionViewModel

@Composable
fun Devoluciones(
    viewModel: DevolucionViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    Scaffold(
        topBar = { Header(title = "DEVOLUCIONES") },
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
                text = "Registrar devolución",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoFormulario(
                value = state.material,
                onValueChange = {
                    viewModel.onMaterialChanged(it)
                },
                label = "Material",
                icono = Icons.Default.Build,
                error = state.materialError,
                placeholder = "Ej: Proyector",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoFormulario(
                value = state.cantidad,
                onValueChange = {
                    viewModel.onCantidadChanged(it)
                },
                label = "Cantidad",
                icono = Icons.Default.Info,
                error = state.cantidadError,
                placeholder = "Ej: 1",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoFormulario(
                value = state.sala,
                onValueChange = {
                    viewModel.onSalaChanged(it)
                },
                label = "Sala de origen",
                icono = Icons.Default.Place,
                error = state.salaError,
                placeholder = "Ej: Sala 204",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Estado del material",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    viewModel.onEstadoChanged("Bueno")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Bueno")
            }

            Button(
                onClick = {
                    viewModel.onEstadoChanged("Dañado")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Dañado")
            }

            Button(
                onClick = {
                    viewModel.onEstadoChanged("Incompleto")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Incompleto")
            }

            if (state.estado.isBlank()) {
                Text("Todavía no seleccionas un estado.")
            } else {
                Text("Estado seleccionado: ${state.estado}")
            }

            if (state.estadoError != null) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = state.estadoError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CampoFormulario(
                value = state.observaciones,
                onValueChange = {
                    viewModel.onObservacionesChanged(it)
                },
                label = "Observaciones",
                icono = Icons.Default.Edit,
                error = state.observacionesError,
                placeholder = "Describe el problema del material",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.onRegistrarClick(
                        context = context,
                        onSuccess = {
                            vibrar(context, 500)
                        },
                        onError = {
                            vibrar(context, 150)
                        }
                    )
                },
                enabled = !viewModel.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.guardando) {
                    Text("Guardando...")
                } else {
                    Text("Registrar devolución")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.registroExitoso) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.mensaje,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}