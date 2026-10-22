package com.example.mpazpanol.model

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