package com.rpmates.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpmates.viewmodel.PlayListViewModel
import com.rpmates.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onPlaylistClick: (Int) -> Unit,
    viewModel: PlayListViewModel,
    userViewModel: UserViewModel
) {
    val favoritePlaylistsState by viewModel.favoritePlaylistsState.collectAsState()
    val currentUser by userViewModel.currentUser.collectAsState()

    // Cargar favoritos cuando se abre la pantalla
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            viewModel.loadFavoritePlaylistsForUser(user.id)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Barra superior
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Favoritos",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2D2D2D)
                )
            )

            when (favoritePlaylistsState) {
                is FavoritePlaylistsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is FavoritePlaylistsState.Success -> {
                    if (favoritePlaylistsState.playlists.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    text = "No tienes playlists favoritas",
                                    color = Color.Gray,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "Marca algunas playlists como favoritas para verlas aquí",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(favoritePlaylistsState.playlists) { playlist ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onPlaylistClick(playlist.id) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF2D2D2D)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = playlist.titulo,
                                                    color = Color.White,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = playlist.descripcion,
                                                    color = Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                                if (playlist.genre.isNotEmpty()) {
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "Género: ${playlist.genre}",
                                                        color = Color(0xFF4CAF50),
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = "Favorito",
                                                tint = Color(0xFFFF6B6B),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                is FavoritePlaylistsState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Error al cargar favoritos",
                                color = Color(0xFFFF6B6B),
                                fontSize = 18.sp
                            )
                            Button(
                                onClick = {
                                    currentUser?.let { user ->
                                        viewModel.loadFavoritePlaylistsForUser(user.id)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Estados para manejar la carga de favoritos
sealed class FavoritePlaylistsState {
    object Loading : FavoritePlaylistsState()
    data class Success(val playlists: List<com.rpmates.data.local.entities.Playlist>) : FavoritePlaylistsState()
    data class Error(val message: String) : FavoritePlaylistsState()
}