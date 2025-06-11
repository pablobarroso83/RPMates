package com.rpmates.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rpmates.data.Repository
import com.rpmates.data.local.dao.ComentarioDao
import com.rpmates.data.local.dao.FavoritaDao
import com.rpmates.data.local.dao.PlaylistDao
import com.rpmates.data.local.dao.UserDao
import com.rpmates.data.local.entities.*
import com.rpmates.util.StringListConverter

@Database(
    entities = [User::class, Playlist::class, Favorita::class, Comentario::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun favoritaDao(): FavoritaDao
    abstract fun comentarioDao(): ComentarioDao


    // Pertenece a la clase noa  la instancia
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rpmates_database"
                )
                    .fallbackToDestructiveMigration(false) // Esto borra la base de datos si subo la version
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
