package com.example.mpazpanol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prestamos")
data class PrestamoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val material: String,
    val cantidad: Int,
    val sala: String,
    val fecha: Long
)