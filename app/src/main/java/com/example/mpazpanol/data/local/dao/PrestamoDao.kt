package com.example.mpazpanol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpazpanol.data.local.entity.PrestamoEntity

@Dao
interface PrestamoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarPrestamo(prestamo: PrestamoEntity)

    @Query("SELECT COUNT(*) FROM prestamos")
    suspend fun contarPrestamos(): Int
}