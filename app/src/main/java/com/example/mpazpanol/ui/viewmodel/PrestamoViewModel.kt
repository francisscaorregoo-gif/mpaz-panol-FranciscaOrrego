package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.data.local.entity.PrestamoEntity
import com.example.mpazpanol.model.INVENTARIO_DISPONIBLE
import com.example.mpazpanol.model.PrestamoModel
import kotlinx.coroutines.launch

class PrestamoViewModel : ViewModel() {

    var uiState by mutableStateOf(PrestamoModel())
        private set

    fun onMaterialChanged(material: String) {
        uiState = uiState.copy(material = material, materialError = null, solicitudExitosa = false)
    }

    fun onCantidadChanged(cantidad: String) {

        val soloNumeros = cantidad.filter { it.isDigit() }.take(2)
        uiState = uiState.copy(cantidad = soloNumeros, cantidadError = null, solicitudExitosa = false)
    }

    fun onSalaChanged(sala: String) {
        uiState = uiState.copy(sala = sala, salaError = null, solicitudExitosa = false)
    }

    fun cargarTotal(context: Context) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            uiState = uiState.copy(total = db.prestamoDao().contarPrestamos())
        }
    }

    private fun validar(state: PrestamoModel): PrestamoModel {
        // busca el material en el inventario sin importar mayúsculas
        val encontrado = INVENTARIO_DISPONIBLE.entries.firstOrNull {
            it.key.equals(state.material.trim(), ignoreCase = true)
        }
        val cantidadNumero = state.cantidad.toIntOrNull()

        return state.copy(
            materialError = when {
                state.material.isBlank() -> "Debes ingresar el material."
                encontrado == null -> "Ese material no existe en el inventario."
                else -> null
            },
            cantidadError = when {
                state.cantidad.isBlank() -> "Debes ingresar la cantidad."
                cantidadNumero == null || cantidadNumero < 1 -> "La cantidad debe ser mayor a 0."
                encontrado != null && cantidadNumero > encontrado.value ->
                    "Solo hay ${encontrado.value} disponibles de ${encontrado.key}."
                else -> null
            },
            salaError = if (state.sala.isBlank()) "Debes ingresar la sala." else null
        )
    }

    private fun tieneErrores(s: PrestamoModel): Boolean =
        s.materialError != null || s.cantidadError != null || s.salaError != null


    fun onSolicitarClick(context: Context, onSuccess: () -> Unit, onError: () -> Unit) {
        val validado = validar(uiState)

        if (tieneErrores(validado)) {
            uiState = validado.copy(solicitudExitosa = false)
            onError()
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)

            val nombreOficial = INVENTARIO_DISPONIBLE.keys.first {
                it.equals(validado.material.trim(), ignoreCase = true)
            }
            db.prestamoDao().registrarPrestamo(
                PrestamoEntity(
                    material = nombreOficial,
                    cantidad = validado.cantidad.toInt(),
                    sala = validado.sala.trim(),
                    fecha = System.currentTimeMillis()
                )
            )
            val total = db.prestamoDao().contarPrestamos()
            uiState = PrestamoModel(solicitudExitosa = true, total = total)
            onSuccess()
        }
    }
}