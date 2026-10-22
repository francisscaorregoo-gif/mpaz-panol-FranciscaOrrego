package com.example.mpazpanol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpazpanol.data.local.entity.DevolucionEntity

@Dao
interface DevolucionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarDevolucion(devolucion: DevolucionEntity)

    @Query("SELECT * FROM devoluciones WHERE id = :numero LIMIT 1")
    suspend fun buscarPorNumero(numero: String): DevolucionEntity?

    @Query("SELECT * FROM devoluciones WHERE fecha = :fecha LIMIT 1")
    suspend fun buscarPorFecha(fecha: Long): DevolucionEntity?
}