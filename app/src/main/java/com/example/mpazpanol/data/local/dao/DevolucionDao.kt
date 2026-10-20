package com.example.mpazpanol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpazpanol.data.local.entity.DevolucionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DevolucionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarDevolucion(devolucion: DevolucionEntity)

    @Query("SELECT * FROM devoluciones ORDER BY fecha DESC")
    fun obtenerDevoluciones(): Flow<List<DevolucionEntity>>
}