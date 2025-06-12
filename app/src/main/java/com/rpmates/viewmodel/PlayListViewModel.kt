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
import com.rpmates.screens.FavoritePlaylistsState
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

    // Estados para búsqueda y paginación
    private val _searchResults = MutableStateFlow<List<PlaylistConFavorito>>(emptyList())
    val searchResults: StateFlow<List<PlaylistConFavorito>> = _searchResults

    private val _isSearchLoading = MutableStateFlow(false)
    val isSearchLoading: StateFlow<Boolean> = _isSearchLoading

    private var currentSearchQuery = ""
    private var currentSortBy = "date_desc"
    private var currentOffset = 0
    private val pageSize = 20

    // Estados para favoritos
    private val _favoritePlaylistsState = MutableStateFlow<FavoritePlaylistsState>(FavoritePlaylistsState.Loading)
    val favoritePlaylistsState: StateFlow<FavoritePlaylistsState> = _favoritePlaylistsState

    val allPlaylists: Flow<List<PlaylistConFavorito>> = _currentUser.flatMapLatest { user ->
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
            _currentUser.value?.let { user ->
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
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                val playlistWithUser = playlist.copy(createdBy = user.id)
                repository.insertPlaylist(playlistWithUser)
            }
        }
    }

    // Búsqueda con paginación
    fun searchPlaylists(query: String, sortBy: String = "date_desc") {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                _isSearchLoading.value = true
                currentSearchQuery = query
                currentSortBy = sortBy
                currentOffset = 0
                
                try {
                    repository.searchPlaylists(
                        userId = user.id,
                        searchQuery = query,
                        sortBy = sortBy,
                        limit = pageSize,
                        offset = 0
                    ).collect { results ->
                        _searchResults.value = results
                        _isSearchLoading.value = false
                    }
                } catch (e: Exception) {
                    _isSearchLoading.value = false
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadMoreSearchResults() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                if (_isSearchLoading.value) return@launch
                
                _isSearchLoading.value = true
                currentOffset += pageSize
                
                try {
                    repository.searchPlaylists(
                        userId = user.id,
                        searchQuery = currentSearchQuery,
                        sortBy = currentSortBy,
                        limit = pageSize,
                        offset = currentOffset
                    ).collect { newResults ->
                        _searchResults.value = _searchResults.value + newResults
                        _isSearchLoading.value = false
                    }
                } catch (e: Exception) {
                    _isSearchLoading.value = false
                    e.printStackTrace()
                }
            }
        }
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
        currentOffset = 0
    }

    // Favoritos
    fun loadFavoritePlaylistsForUser(userId: Int) {
        viewModelScope.launch {
            _favoritePlaylistsState.value = FavoritePlaylistsState.Loading
            try {
                repository.getFavoritasForUser(userId).collect { playlists ->
                    _favoritePlaylistsState.value = FavoritePlaylistsState.Success(playlists)
                }
            } catch (e: Exception) {
                _favoritePlaylistsState.value = FavoritePlaylistsState.Error(e.message ?: "Error desconocido")
            }
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

    // Editar y eliminar playlists
    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                repository.updatePlaylist(playlist)
                // Actualizar la playlist actual si es la que se está editando
                _currentUser.value?.let { user ->
                    repository.getPlaylistByIdWithFavorito(playlist.id, user.id)?.let { updatedPlaylist ->
                        _currentPlaylist.value = updatedPlaylist
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                repository.deletePlaylist(playlist)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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