package com.rpmates.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rpmates.data.local.entities.Comentario
import com.rpmates.data.local.models.ComentarioConUsuario
import kotlinx.coroutines.flow.Flow

@Dao
interface ComentarioDao {

    @Insert
    suspend fun insertComentario(comentario: Comentario): Long

    @Query("SELECT * FROM comentarios WHERE playlistId = :playlistId")
    fun getComentariosByPlaylist(playlistId: Int): Flow<List<Comentario>>

    // Obtener comentarios con nombre de usuario
    @Query("""
        SELECT c.id, c.playlistId, c.userId, c.texto, u.username
        FROM comentarios c
        INNER JOIN users u ON c.userId = u.id
        WHERE c.playlistId = :playlistId
        ORDER BY c.id ASC
    """)
    fun getComentariosConUsuario(playlistId: Int): Flow<List<ComentarioConUsuario>>
}
