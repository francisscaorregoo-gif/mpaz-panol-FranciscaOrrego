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
import kotlinx.coroutines.launch

class DevolucionViewModel : ViewModel() {

    var uiState by mutableStateOf(DevolucionModel())
        private set

    var guardando by mutableStateOf(false)
        private set

    var mensaje by mutableStateOf("")
        private set

    fun onMaterialChanged(material: String) {
        uiState = uiState.copy(
            material = material,
            materialError = null,
            registroExitoso = false
        )
        mensaje = ""
    }

    fun onCantidadChanged(cantidad: String) {
        uiState = uiState.copy(
            cantidad = cantidad,
            cantidadError = null,
            registroExitoso = false
        )
        mensaje = ""
    }

    fun onSalaChanged(sala: String) {
        uiState = uiState.copy(
            sala = sala,
            salaError = null,
            registroExitoso = false
        )
        mensaje = ""
    }

    fun onEstadoChanged(estado: String) {
        uiState = uiState.copy(
            estado = estado,
            estadoError = null,
            observacionesError = null,
            registroExitoso = false
        )
        mensaje = ""
    }

    fun onObservacionesChanged(observaciones: String) {
        uiState = uiState.copy(
            observaciones = observaciones,
            observacionesError = null,
            registroExitoso = false
        )
        mensaje = ""
    }

    fun onRegistrarClick(
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (guardando) return

        val state = uiState
        val cantidad = state.cantidad.toIntOrNull()

        var errorMaterial: String? = null
        var errorCantidad: String? = null
        var errorSala: String? = null
        var errorEstado: String? = null
        var errorObservaciones: String? = null

        if (state.material.isBlank()) {
            errorMaterial = "Debes ingresar el material devuelto."
        } else if (state.material.trim().length < 3) {
            errorMaterial = "El material debe tener al menos 3 caracteres."
        }

        if (state.cantidad.isBlank()) {
            errorCantidad = "Debes ingresar la cantidad."
        } else if (cantidad == null || cantidad < 1) {
            errorCantidad = "Ingresa una cantidad entera mayor a 0."
        }

        if (state.sala.isBlank()) {
            errorSala = "Debes ingresar la sala de origen."
        }

        if (state.estado.isBlank()) {
            errorEstado = "Selecciona el estado del material."
        } else if (
            state.estado != "Bueno" &&
            state.estado != "Dañado" &&
            state.estado != "Incompleto"
        ) {
            errorEstado = "Selecciona un estado válido."
        }

        if (
            state.estado == "Dañado" ||
            state.estado == "Incompleto"
        ) {
            if (state.observaciones.trim().length < 10) {
                errorObservaciones =
                    "Describe el problema con al menos 10 caracteres."
            }
        }

        uiState = state.copy(
            materialError = errorMaterial,
            cantidadError = errorCantidad,
            salaError = errorSala,
            estadoError = errorEstado,
            observacionesError = errorObservaciones,
            registroExitoso = false
        )

        mensaje = ""

        if (
            errorMaterial != null ||
            errorCantidad != null ||
            errorSala != null ||
            errorEstado != null ||
            errorObservaciones != null
        ) {
            onError()
            return
        }

        if (cantidad == null) return

        guardando = true

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val fechaRegistro = System.currentTimeMillis()

            db.devolucionDao().registrarDevolucion(
                DevolucionEntity(
                    material = state.material.trim(),
                    cantidad = cantidad,
                    sala = state.sala.trim(),
                    estado = state.estado,
                    observaciones = state.observaciones.trim(),
                    fecha = fechaRegistro
                )
            )

            val registro = db.devolucionDao()
                .buscarPorFecha(fechaRegistro)

            uiState = DevolucionModel(
                registroExitoso = true
            )

            if (registro != null) {
                mensaje = "Devolución registrada. Número: ${registro.id}. " +
                        "Puedes consultarla en Historial."
            } else {
                mensaje = "Devolución registrada correctamente."
            }

            guardando = false
            onSuccess()
        }
    }
}