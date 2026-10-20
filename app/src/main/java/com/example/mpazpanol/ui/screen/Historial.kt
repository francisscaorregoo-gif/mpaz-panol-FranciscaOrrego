package com.example.mpazpanol.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpazpanol.model.ESTADOS_DEVOLUCION
import com.example.mpazpanol.model.FILTRO_TODOS
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.viewmodel.HistorialViewModel

@Composable
fun Historial(
    viewModel: HistorialViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.observarHistorial(context) }

    val filtradas = viewModel.devolucionesFiltradas()

    Scaffold(
        topBar = { Header(title = "HISTORIAL") },
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
                    text = "Historial de devoluciones",
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }


            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ResumenItem("Total", viewModel.devoluciones.size)
                        ResumenItem("Bueno", viewModel.contarPorEstado("Bueno"))
                        ResumenItem("Dañado", viewModel.contarPorEstado("Dañado"))
                        ResumenItem("Incompleto", viewModel.contarPorEstado("Incompleto"))
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (listOf(FILTRO_TODOS) + ESTADOS_DEVOLUCION).forEach { opcion ->
                        FilterChip(
                            selected = state.filtroEstado == opcion,
                            onClick = { viewModel.onFiltroChanged(opcion) },
                            label = { Text(opcion) }
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resultados (${filtradas.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                    AnimatedVisibility(visible = viewModel.hayFiltrosActivos()) {
                        TextButton(onClick = { viewModel.limpiarFiltros() }) {
                            Text("Limpiar filtros")
                        }
                    }
                }
            }

            if (filtradas.isEmpty()) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                        Text(
                            text = if (viewModel.devoluciones.isEmpty())
                                "Aún no hay devoluciones registradas."
                            else
                                "No hay resultados para la búsqueda o el filtro aplicado."
                        )
                    }
                }
            } else {
                items(filtradas, key = { it.id }) { devolucion ->
                    TarjetaDevolucion(devolucion)
                }
            }
        }
    }
}

@Composable
private fun ResumenItem(titulo: String, cantidad: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = cantidad.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(text = titulo, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun HistorialPreview() {
    Historial()
}