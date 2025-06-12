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
        WHERE (:searchQuery IS NULL OR p.titulo LIKE '%' || :searchQuery || '%' 
               OR p.descripcion LIKE '%' || :searchQuery || '%'
               OR p.genre LIKE '%' || :searchQuery || '%')
        ORDER BY 
        CASE WHEN :sortBy = 'title_asc' THEN p.titulo END ASC,
        CASE WHEN :sortBy = 'title_desc' THEN p.titulo END DESC,
        CASE WHEN :sortBy = 'date_asc' THEN p.createdAt END ASC,
        CASE WHEN :sortBy = 'date_desc' THEN p.createdAt END DESC,
        p.id DESC
        LIMIT :limit OFFSET :offset
    """)
    fun getAllPlaylistsWithFavorito(
        userId: Int, 
        searchQuery: String? = null,
        sortBy: String = "date_desc",
        limit: Int = 20,
        offset: Int = 0
    ): Flow<List<PlaylistConFavorito>>

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

    @Query("SELECT COUNT(*) FROM playlists WHERE (:searchQuery IS NULL OR titulo LIKE '%' || :searchQuery || '%')")
    suspend fun getPlaylistCount(searchQuery: String? = null): Int

    @Query("SELECT * FROM playlists WHERE createdBy = :userId")
    fun getPlaylistsByUser(userId: Int): Flow<List<Playlist>>
}