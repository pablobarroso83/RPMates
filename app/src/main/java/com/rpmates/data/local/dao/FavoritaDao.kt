package com.rpmates.data.local.dao

import androidx.room.*
import com.rpmates.data.local.entities.Favorita
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorita(favorita: Favorita)

    @Query("DELETE FROM favoritas WHERE userId = :userId AND playlistId = :playlistId")
    suspend fun removeFavorita(userId: Int, playlistId: Int)

    @Query("""
        SELECT playlists.* FROM playlists
        INNER JOIN favoritas ON playlists.id = favoritas.playlistId
        WHERE favoritas.userId = :userId
    """)
    fun getFavoritasForUser(userId: Int): Flow<List<com.rpmates.data.local.entities.Playlist>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritas WHERE userId = :userId AND playlistId = :playlistId)")
    suspend fun isFavorita(userId: Int, playlistId: Int): Boolean
}
