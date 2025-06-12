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
    version = 3,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun favoritaDao(): FavoritaDao
    abstract fun comentarioDao(): ComentarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migración de versión 2 a 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar nuevas columnas a la tabla users
                database.execSQL("ALTER TABLE users ADD COLUMN email TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN firstName TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN lastName TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN phoneNumber TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN dateOfBirth TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE users ADD COLUMN isAdmin INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE users ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")

                // Agregar nuevas columnas a la tabla playlists
                database.execSQL("ALTER TABLE playlists ADD COLUMN createdBy INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE playlists ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE playlists ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE playlists ADD COLUMN isPublic INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE playlists ADD COLUMN genre TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE playlists ADD COLUMN tags TEXT NOT NULL DEFAULT '[]'")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rpmates_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}