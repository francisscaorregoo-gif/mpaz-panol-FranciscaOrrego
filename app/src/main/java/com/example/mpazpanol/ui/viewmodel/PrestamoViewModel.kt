package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.data.local.entity.PrestamoEntity
import com.example.mpazpanol.model.PrestamoModel
import kotlinx.coroutines.launch

class PrestamoViewModel : ViewModel() {

    var uiState by mutableStateOf(PrestamoModel())
        private set

    var totalCargado by mutableStateOf(false)
        private set

    fun onMaterialChanged(material: String) {
        uiState = uiState.copy(
            material = material,
            materialError = null,
            cantidadError = null,
            solicitudExitosa = false
        )
    }

    fun onCantidadChanged(cantidad: String) {
        uiState = uiState.copy(
            cantidad = cantidad,
            cantidadError = null,
            solicitudExitosa = false
        )
    }

    fun onSalaChanged(sala: String) {
        uiState = uiState.copy(
            sala = sala,
            salaError = null,
            solicitudExitosa = false
        )
    }

    fun cargarTotal(context: Context) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)

            uiState = uiState.copy(
                total = db.prestamoDao().contarPrestamos()
            )

            totalCargado = true
        }
    }

    private fun limitePorMaterial(material: String): Int {
        if (material == "Proyector") {
            return 3
        }

        if (material == "Notebook") {
            return 5
        }

        if (material == "Cable HDMI") {
            return 8
        }

        if (material == "Parlante") {
            return 4
        }

        if (material == "Control remoto") {
            return 6
        }

        return 0
    }

    // Ningún material permite más de 8 unidades por solicitud.
    // Reconocemos las cantidades permitidas mediante condiciones.
    private fun obtenerCantidad(texto: String): Int {
        if (texto == "1") {
            return 1
        }

        if (texto == "2") {
            return 2
        }

        if (texto == "3") {
            return 3
        }

        if (texto == "4") {
            return 4
        }

        if (texto == "5") {
            return 5
        }

        if (texto == "6") {
            return 6
        }

        if (texto == "7") {
            return 7
        }

        if (texto == "8") {
            return 8
        }

        return 0
    }

    fun onSolicitarClick(
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val state = uiState

        val limite = limitePorMaterial(state.material)
        val cantidad = obtenerCantidad(state.cantidad)

        var errorMaterial: String? = null
        var errorCantidad: String? = null
        var errorSala: String? = null

        if (state.material.isBlank()) {
            errorMaterial = "Debes ingresar el material."
        } else if (limite == 0) {
            errorMaterial =
                "Escribe el nombre exactamente como aparece en la lista."
        }

        if (state.cantidad.isBlank()) {
            errorCantidad = "Debes ingresar la cantidad."
        } else if (cantidad == 0) {
            errorCantidad = "Ingresa un número entero del 1 al 8."
        } else if (limite > 0 && cantidad > limite) {
            errorCantidad = "El máximo por solicitud es $limite."
        }

        if (state.sala.isBlank()) {
            errorSala = "Debes ingresar la sala."
        }

        uiState = state.copy(
            materialError = errorMaterial,
            cantidadError = errorCantidad,
            salaError = errorSala,
            solicitudExitosa = false
        )

        if (
            errorMaterial != null ||
            errorCantidad != null ||
            errorSala != null
        ) {
            onError()
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)

            db.prestamoDao().registrarPrestamo(
                PrestamoEntity(
                    material = state.material,
                    cantidad = cantidad,
                    sala = state.sala,
                    fecha = System.currentTimeMillis()
                )
            )

            val total = db.prestamoDao().contarPrestamos()

            uiState = PrestamoModel(
                solicitudExitosa = true,
                total = total
            )

            totalCargado = true
            onSuccess()
        }
    }
}