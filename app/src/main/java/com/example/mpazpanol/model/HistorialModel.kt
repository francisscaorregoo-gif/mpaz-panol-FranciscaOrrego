package com.example.mpazpanol.model

const val FILTRO_TODOS = "Todos"

data class HistorialModel(
    val busqueda: String = "",
    val filtroEstado: String = FILTRO_TODOS
)