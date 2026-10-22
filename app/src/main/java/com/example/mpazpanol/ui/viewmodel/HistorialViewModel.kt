package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.data.local.entity.DevolucionEntity
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {

    var numero by mutableStateOf("")
        private set

    var errorNumero by mutableStateOf<String?>(null)
        private set

    var devolucion by mutableStateOf<DevolucionEntity?>(null)
        private set

    var buscando by mutableStateOf(false)
        private set

    fun onNumeroChanged(texto: String) {
        numero = texto
        errorNumero = null
        devolucion = null
    }

    fun buscarDevolucion(context: Context) {
        if (buscando) return

        val numeroBuscado = numero

        errorNumero = null
        devolucion = null

        if (numeroBuscado.isBlank()) {
            errorNumero = "Ingresa el número de la devolución."
            return
        }

        buscando = true

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)

            val resultado = db.devolucionDao()
                .buscarPorNumero(numeroBuscado)

            // Evita mostrar un resultado si se cambió el campo
            // mientras se realizaba la consulta.
            if (numero == numeroBuscado) {
                if (resultado != null) {
                    devolucion = resultado
                } else {
                    errorNumero =
                        "No se encontró una devolución con ese número."
                }
            }

            buscando = false
        }
    }
}