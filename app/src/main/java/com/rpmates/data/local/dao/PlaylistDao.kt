package com.rpmates.data.local.dao

import androidx.room.*
import com.rpmates.data.local.entities.Playlist
import com.rpmates.data.local.models.PlaylistConFavorito
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Query("""
        SELECT p.*, 
        CASE WHEN f.userId IS NOT NULL THEN 1 ELSE 0 END as esFavorita
        FROM playlists p
        LEFT JOIN favoritas f ON p.id = f.playlistId AND f.userId = :userId
    """)
    fun getAllPlaylistsWithFavorito(userId: Int): Flow<List<PlaylistConFavorito>>

    @Query("""
        SELECT p.*, 
        CASE WHEN f.userId IS NOT NULL THEN 1 ELSE 0 END as esFavorita
        FROM playlists p
        LEFT JOIN favoritas f ON p.id = f.playlistId AND f.userId = :userId
        WHERE p.id = :id
    """)
    suspend fun getPlaylistByIdWithFavorito(id: Int, userId: Int): PlaylistConFavorito?

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)
}
