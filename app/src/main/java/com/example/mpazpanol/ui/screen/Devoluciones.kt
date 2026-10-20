package com.example.mpazpanol.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpazpanol.data.local.entity.DevolucionEntity
import com.example.mpazpanol.model.ESTADOS_DEVOLUCION
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.components.vibrar
import com.example.mpazpanol.ui.viewmodel.DevolucionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue

@Composable
fun Devoluciones(
    viewModel: DevolucionViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    // empieza a leer las devoluciones guardadas en SQLite (una sola vez)
    LaunchedEffect(Unit) { viewModel.observarDevoluciones(context) }

    Scaffold(
        topBar = { Header(title = "DEVOLUCIONES") },
        bottomBar = { Footer() }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Registrar devolución",
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                CampoDevolucion(
                    value = state.material,
                    onValueChange = { viewModel.onMaterialChanged(it) },
                    label = "Material",
                    placeholder = "Ej: Proyector",
                    icono = Icons.Default.Build,
                    error = state.materialError
                )
            }

            item {
                CampoDevolucion(
                    value = state.cantidad,
                    onValueChange = { viewModel.onCantidadChanged(it) },
                    label = "Cantidad",
                    placeholder = "Ej: 1",
                    icono = Icons.Default.Info,
                    error = state.cantidadError,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                CampoDevolucion(
                    value = state.sala,
                    onValueChange = { viewModel.onSalaChanged(it) },
                    label = "Sala de origen",
                    placeholder = "Ej: Sala 204",
                    icono = Icons.Default.Place,
                    error = state.salaError
                )
            }

            // ----- estado del material -----
            item {
                Column {
                    Text("Estado del material", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ESTADOS_DEVOLUCION.forEach { opcion ->
                            val seleccionado = state.estado == opcion
                            FilterChip(
                                selected = seleccionado,
                                onClick = { viewModel.onEstadoChanged(opcion) },
                                label = { Text(opcion) },
                                leadingIcon = if (seleccionado) {
                                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                                } else null
                            )
                        }
                    }
                    if (state.estadoError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            Text(
                                text = state.estadoError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            item {
                CampoDevolucion(
                    value = state.observaciones,
                    onValueChange = { viewModel.onObservacionesChanged(it) },
                    label = "Observaciones",
                    placeholder = "Ej: Cable HDMI con el conector doblado",
                    icono = Icons.Default.Edit,
                    error = state.observacionesError,
                    singleLine = false,
                    minLines = 3
                )
            }

            item {
                Button(
                    onClick = {
                        viewModel.onRegistrarClick(
                            context = context,
                            onSuccess = { vibrar(context, 500) },   // recurso nativo: vibración
                            onError = { vibrar(context, 150) }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                ) {
                    Text("Registrar devolución")
                }
            }
            item {
                AnimatedVisibility(visible = state.registroExitoso) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                            Text("Devolución registrada correctamente")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Devoluciones registradas (${viewModel.devoluciones.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (viewModel.devoluciones.isEmpty()) {
                item { Text("Aún no hay devoluciones registradas.") }
            } else {
                items(viewModel.devoluciones, key = { it.id }) { devolucion ->
                    TarjetaDevolucion(devolucion)
                }
            }
        }
    }
}

@Composable
private fun CampoDevolucion(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icono: ImageVector,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = {
            if (error != null) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TarjetaDevolucion(devolucion: DevolucionEntity) {
    val colorFondo by animateColorAsState(
        targetValue = when (devolucion.estado) {
            "Bueno" -> Color(0xFFE8F5E9)
            "Dañado" -> Color(0xFFFFEBEE)
            else -> Color(0xFFFFF8E1)
        },
        label = "colorEstado"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = colorFondo,
            contentColor = Color.Black
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${devolucion.material} (x${devolucion.cantidad})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text("Sala: ${devolucion.sala}")
            Text("Estado: ${devolucion.estado}")
            if (devolucion.observaciones.isNotBlank()) {
                Text("Observación: ${devolucion.observaciones}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DevolucionesPreview() {
    Devoluciones()
}