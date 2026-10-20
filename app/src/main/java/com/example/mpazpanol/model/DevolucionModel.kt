package com.example.mpazpanol.model

val ESTADOS_DEVOLUCION = listOf("Bueno", "Dañado", "Incompleto")

data class DevolucionModel(
    val material: String = "",
    val cantidad: String = "",
    val sala: String = "",
    val estado: String = "",
    val observaciones: String = "",

    val materialError: String? = null,
    val cantidadError: String? = null,
    val salaError: String? = null,
    val estadoError: String? = null,
    val observacionesError: String? = null,

    val registroExitoso: Boolean = false
)