package com.rpmates.data

import com.rpmates.data.local.dao.ComentarioDao
import com.rpmates.data.local.dao.FavoritaDao
import com.rpmates.data.local.dao.PlaylistDao
import com.rpmates.data.local.dao.UserDao
import com.rpmates.data.local.entities.Comentario
import com.rpmates.data.local.entities.Favorita
import com.rpmates.data.local.entities.Playlist
import com.rpmates.data.local.entities.User
import com.rpmates.data.local.models.PlaylistConFavorito
import kotlinx.coroutines.flow.Flow

class Repository(
    private val playlistDao: PlaylistDao,
    private val comentarioDao: ComentarioDao,
    private val userDao: UserDao,
    private val favoritaDao: FavoritaDao
) {

    // Playlists
    fun getAllPlaylists(userId: Int): Flow<List<PlaylistConFavorito>> = 
        playlistDao.getAllPlaylistsWithFavorito(userId)

    suspend fun insertPlaylist(playlist: Playlist) = playlistDao.insertPlaylist(playlist)


    suspend fun getPlaylistByIdWithFavorito(id: Int, userId: Int): PlaylistConFavorito? {
        return playlistDao.getPlaylistByIdWithFavorito(id, userId)
    }

    suspend fun toggleFavorita(userId: Int, playlistId: Int) {
        try {
            if (favoritaDao.isFavorita(userId, playlistId)) {
                favoritaDao.removeFavorita(userId, playlistId)
            } else {
                favoritaDao.addFavorita(Favorita(userId, playlistId))
            }
        } catch (e: Exception) {
            throw Exception("Error al actualizar favorito: ${e.message}")
        }
    }

    // Usuarios
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)

    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)

    suspend fun getUserById(id: Int): User? = userDao.getUserById(id)

    // Comentarios
    fun getComentariosConUsuario(playlistId: Int) =
        comentarioDao.getComentariosConUsuario(playlistId)

    suspend fun insertComentario(comentario: Comentario) = comentarioDao.insertComentario(comentario)

}
