package com.rpmates

import android.app.Application
import com.rpmates.data.Repository
import com.rpmates.data.local.database.AppDatabase
//import com.rpmates.data.local.database.DatabaseInitializer

class RPMatesApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        Repository(
            playlistDao = database.playlistDao(),
            comentarioDao = database.comentarioDao(),
            userDao = database.userDao(),
            favoritaDao = database.favoritaDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        // Inicializar datos de prueba
        // DatabaseInitializer(repository).initializeDatabase()
    }
} 