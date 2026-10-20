package com.example.mpazpanol.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mpazpanol.data.local.dao.DevolucionDao
import com.example.mpazpanol.data.local.dao.PrestamoDao
import com.example.mpazpanol.data.local.dao.UsuarioDao
import com.example.mpazpanol.data.local.entity.DevolucionEntity
import com.example.mpazpanol.data.local.entity.PrestamoEntity
import com.example.mpazpanol.data.local.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, DevolucionEntity::class, PrestamoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun devolucionDao(): DevolucionDao
    abstract fun prestamoDao(): PrestamoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Myapp_database.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}