package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.data.local.entity.DevolucionEntity
import com.example.mpazpanol.model.DevolucionModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DevolucionViewModel : ViewModel() {

    var uiState by mutableStateOf(DevolucionModel())
        private set

    var devoluciones by mutableStateOf<List<DevolucionEntity>>(emptyList())
        private set

    private var observando = false


    fun onMaterialChanged(material: String) {
        uiState = uiState.copy(material = material, materialError = null, registroExitoso = false)
    }

    fun onCantidadChanged(cantidad: String) {
        val soloNumeros = cantidad.filter { it.isDigit() }.take(2)
        uiState = uiState.copy(cantidad = soloNumeros, cantidadError = null, registroExitoso = false)
    }

    fun onSalaChanged(sala: String) {
        uiState = uiState.copy(sala = sala, salaError = null, registroExitoso = false)
    }

    fun onEstadoChanged(estado: String) {
        uiState = uiState.copy(estado = estado, estadoError = null, observacionesError = null, registroExitoso = false)
    }

    fun onObservacionesChanged(observaciones: String) {
        uiState = uiState.copy(observaciones = observaciones, observacionesError = null, registroExitoso = false)
    }

    fun observarDevoluciones(context: Context) {
        if (observando) return
        observando = true
        viewModelScope.launch {
            AppDatabase.getDatabase(context).devolucionDao().obtenerDevoluciones().collect {
                devoluciones = it
            }
        }
    }

    private fun validar(state: DevolucionModel): DevolucionModel {
        val cantidadNumero = state.cantidad.toIntOrNull()

        return state.copy(
            materialError = when {
                state.material.isBlank() -> "Debes ingresar el material devuelto."
                state.material.trim().length < 3 -> "El material debe tener al menos 3 caracteres."
                else -> null
            },
            cantidadError = when {
                state.cantidad.isBlank() -> "Debes ingresar la cantidad."
                cantidadNumero == null || cantidadNumero < 1 -> "La cantidad debe ser mayor a 0."
                else -> null
            },
            salaError = if (state.sala.isBlank()) "Debes ingresar la sala de origen." else null,
            estadoError = if (state.estado.isBlank()) "Selecciona el estado del material." else null,
            // si vuelve en mal estado, es obligatorio explicar qué pasó
            observacionesError = if (state.estado.isNotBlank() && state.estado != "Bueno" &&
                state.observaciones.trim().length < 10
            ) "Describe el problema (mínimo 10 caracteres)." else null
        )
    }

    private fun tieneErrores(s: DevolucionModel): Boolean =
        s.materialError != null || s.cantidadError != null || s.salaError != null ||
                s.estadoError != null || s.observacionesError != null

    fun onRegistrarClick(context: Context, onSuccess: () -> Unit, onError: () -> Unit) {
        val validado = validar(uiState)

        if (tieneErrores(validado)) {
            uiState = validado.copy(registroExitoso = false)
            onError()
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            db.devolucionDao().registrarDevolucion(
                DevolucionEntity(
                    material = validado.material.trim(),
                    cantidad = validado.cantidad.toInt(),
                    sala = validado.sala.trim(),
                    estado = validado.estado,
                    observaciones = validado.observaciones.trim(),
                    fecha = System.currentTimeMillis()
                )
            )
            uiState = DevolucionModel(registroExitoso = true)
            onSuccess()
            delay(3000)
            uiState = uiState.copy(registroExitoso = false)
        }
    }
}