package com.example.mpazpanol.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devoluciones")
data class DevolucionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val material: String,
    val cantidad: Int,
    val sala: String,
    val estado: String,
    val observaciones: String,
    val fecha: Long
)