package com.example.mpazpanol.model


val INVENTARIO_DISPONIBLE = mapOf(
    "Proyector" to 3,
    "Notebook" to 5,
    "Cable HDMI" to 8,
    "Parlante" to 4,
    "Control remoto" to 6
)

data class PrestamoModel(
    val material: String = "",
    val cantidad: String = "",
    val sala: String = "",

    val materialError: String? = null,
    val cantidadError: String? = null,
    val salaError: String? = null,

    val solicitudExitosa: Boolean = false,
    val total: Int = 0
)