package com.example.mpazpanol.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpazpanol.model.INVENTARIO_DISPONIBLE
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.components.vibrar
import com.example.mpazpanol.ui.viewmodel.PrestamoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Prestamos(
    viewModel: PrestamoViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    val duracion = 500L
    var vibrando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val transicion = rememberInfiniteTransition(label = "temblor")
    val desplazamiento by transicion.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(40), RepeatMode.Reverse),
        label = "desplazamiento"
    )

    LaunchedEffect(Unit) { viewModel.cargarTotal(context) }

    Scaffold(
        topBar = { Header(title = "PRÉSTAMOS") },
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
                text = "Solicitar material",
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Materiales: " + INVENTARIO_DISPONIBLE.keys.joinToString(", "),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoPrestamo(
                value = state.material,
                onValueChange = { viewModel.onMaterialChanged(it) },
                label = "Material",
                placeholder = "Ej: Proyector",
                icono = Icons.Default.Build,
                error = state.materialError
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoPrestamo(
                value = state.cantidad,
                onValueChange = { viewModel.onCantidadChanged(it) },
                label = "Cantidad",
                placeholder = "Ej: 1",
                icono = Icons.Default.Info,
                error = state.cantidadError,
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoPrestamo(
                value = state.sala,
                onValueChange = { viewModel.onSalaChanged(it) },
                label = "Sala",
                placeholder = "Ej: Sala 204",
                icono = Icons.Default.Place,
                error = state.salaError
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                enabled = !vibrando,
                onClick = {
                    viewModel.onSolicitarClick(
                        context = context,
                        onSuccess = {
                            vibrar(context, duracion)
                            scope.launch {
                                vibrando = true
                                delay(duracion)
                                vibrando = false
                            }
                        },
                        onError = { vibrar(context, 150) }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4))
            ) {
                Text("Solicitar préstamo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.solicitudExitosa) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(if (vibrando) desplazamiento.roundToInt() else 0, 0) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (vibrando) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = if (vibrando) "📳 Solicitud realizada correctamente"
                        else "Solicitud realizada correctamente",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Préstamos registrados: ${state.total}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun CampoPrestamo(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icono: ImageVector,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text
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
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PrestamosPreview() {
    Prestamos()
}