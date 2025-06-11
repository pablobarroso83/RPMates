package com.rpmates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rpmates.data.Repository
import com.rpmates.data.local.entities.Comentario
import com.rpmates.data.local.entities.Playlist
import com.rpmates.data.local.entities.User
import com.rpmates.data.local.models.ComentarioConUsuario
import com.rpmates.data.local.models.PlaylistConFavorito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val repository: Repository,
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val _currentUser = userViewModel.currentUser
    private val _currentPlaylist = MutableStateFlow<PlaylistConFavorito?>(null)
    val currentPlaylist: MutableStateFlow<PlaylistConFavorito?> = _currentPlaylist

    val allPlaylists: Flow<List<PlaylistConFavorito>> = _currentUser.flatMapLatest { user -> // Tiene en cuenta solo el ultimo flow si llega otro se cancela el anterior
        if (user != null) {
            repository.getAllPlaylists(user.id)
        } else {
            flowOf(emptyList())
        }
    }

    fun getPlaylistById(id: Int): Flow<PlaylistConFavorito?> {
        return _currentUser.map { user ->
            if (user != null) {
                repository.getPlaylistByIdWithFavorito(id, user.id)?.also { playlist ->
                    _currentPlaylist.value = playlist
                }
            } else {
                null
            }
        }
    }

    fun toggleFavorita(playlistId: Int) {
        viewModelScope.launch {
            _currentUser.value?.let { user -> // Let para que ejecute solo si no es null
                try {
                    repository.toggleFavorita(user.id, playlistId)
                    userViewModel.refreshUser()
                    repository.getPlaylistByIdWithFavorito(playlistId, user.id)?.let { updatedPlaylist ->
                        _currentPlaylist.value = updatedPlaylist
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch { // Lanza corrutina
            repository.insertPlaylist(playlist)
        }
    }

    // Comentarios
    fun getComentariosConUsuario(playlistId: Int): Flow<List<ComentarioConUsuario>> {
        return try {
            repository.getComentariosConUsuario(playlistId)
        } catch (e: Exception) {
            flowOf(emptyList())
        }
    }

    fun insertComentario(comentario: Comentario) {
        viewModelScope.launch {
            repository.insertComentario(comentario)
        }
    }

    class Factory(
        private val repository: Repository,
        private val userViewModel: UserViewModel
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlayListViewModel::class.java)) {
                return PlayListViewModel(repository, userViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

