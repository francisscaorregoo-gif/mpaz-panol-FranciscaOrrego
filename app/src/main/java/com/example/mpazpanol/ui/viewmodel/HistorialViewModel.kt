package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.data.local.entity.DevolucionEntity
import com.example.mpazpanol.model.FILTRO_TODOS
import com.example.mpazpanol.model.HistorialModel
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    var uiState by mutableStateOf(HistorialModel())
        private set

    var devoluciones by mutableStateOf<List<DevolucionEntity>>(emptyList())
        private set

    private var observando = false

    fun observarHistorial(context: Context) {
        if (observando) return
        observando = true
        viewModelScope.launch {
            AppDatabase.getDatabase(context).devolucionDao().obtenerDevoluciones().collect {
                devoluciones = it
            }
        }
    }

    fun onBusquedaChanged(texto: String) {
        // máximo 30 caracteres
        uiState = uiState.copy(busqueda = texto.take(30))
    }

    fun onFiltroChanged(filtro: String) {
        uiState = uiState.copy(filtroEstado = filtro)
    }

    fun limpiarFiltros() {
        uiState = HistorialModel()
    }

    fun hayFiltrosActivos(): Boolean =
        uiState.busqueda.isNotBlank() || uiState.filtroEstado != FILTRO_TODOS

    fun devolucionesFiltradas(): List<DevolucionEntity> {
        val texto = uiState.busqueda.trim()
        return devoluciones.filter { d ->
            val coincideEstado = uiState.filtroEstado == FILTRO_TODOS || d.estado == uiState.filtroEstado
            val coincideTexto = texto.isEmpty() ||
                    d.material.contains(texto, ignoreCase = true) ||
                    d.sala.contains(texto, ignoreCase = true)
            coincideEstado && coincideTexto
        }
    }

    fun contarPorEstado(estado: String): Int = devoluciones.count { it.estado == estado }
}