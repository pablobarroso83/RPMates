package com.rpmates.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onPlaylistClick: (Int) -> Unit,
    viewModel: PlayListViewModel,
    userViewModel: UserViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("date_desc") }
    var showSortMenu by remember { mutableStateOf(false) }
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isSearchLoading.collectAsState()
    val listState = rememberLazyListState()

    // Búsqueda con debounce
    LaunchedEffect(searchQuery, sortBy) {
        delay(300) // Debounce de 300ms
        if (searchQuery.isNotEmpty()) {
            viewModel.searchPlaylists(searchQuery, sortBy)
        } else {
            viewModel.clearSearchResults()
        }
    }

    // Detectar scroll para paginación
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && 
                    lastVisibleIndex >= searchResults.size - 3 && 
                    searchQuery.isNotEmpty() && 
                    !isLoading) {
                    viewModel.loadMoreSearchResults()
                }
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
            // Barra superior con búsqueda
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar playlists...", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.White
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpiar",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true
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

            // Filtros y ordenación
            if (searchQuery.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${searchResults.size} resultados",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    
                    Box {
                        TextButton(
                            onClick = { showSortMenu = true }
                        ) {
                            Text(
                                text = when (sortBy) {
                                    "title_asc" -> "Título A-Z"
                                    "title_desc" -> "Título Z-A"
                                    "date_asc" -> "Más antiguos"
                                    "date_desc" -> "Más recientes"
                                    else -> "Ordenar"
                                },
                                color = Color.White
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Más recientes") },
                                onClick = {
                                    sortBy = "date_desc"
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Más antiguos") },
                                onClick = {
                                    sortBy = "date_asc"
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Título A-Z") },
                                onClick = {
                                    sortBy = "title_asc"
                                    showSortMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Título Z-A") },
                                onClick = {
                                    sortBy = "title_desc"
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Resultados de búsqueda
            when {
                searchQuery.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = "Busca playlists por título, descripción o género",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                isLoading && searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron resultados para \"$searchQuery\"",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { playlist ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPlaylistClick(playlist.playlist.id) },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF2D2D2D)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = playlist.playlist.titulo,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = playlist.playlist.descripcion,
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    if (playlist.playlist.genre.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Género: ${playlist.playlist.genre}",
                                            color = Color(0xFF4CAF50),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                        
                        if (isLoading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}