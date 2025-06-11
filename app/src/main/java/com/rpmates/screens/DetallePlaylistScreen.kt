package com.rpmates.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpmates.data.local.entities.Comentario
import com.rpmates.viewmodel.PlayListViewModel
import com.rpmates.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun DetallePlaylistScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: PlayListViewModel,
    userViewModel: UserViewModel
) {
    val playlist by viewModel.currentPlaylist.collectAsState()
    val comentarios by viewModel.getComentariosConUsuario(id).collectAsState(initial = emptyList())
    val currentUser by userViewModel.currentUser.collectAsState()
    var nuevoComentario by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Cargar la playlist inicial
    LaunchedEffect(id) {
        viewModel.getPlaylistById(id).collect { updatedPlaylist ->
            viewModel.currentPlaylist.value = updatedPlaylist
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                playlist?.let { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                text = p.playlist.titulo,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = p.playlist.descripcion,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                            )
                            Text(
                                text = "Vinilos:",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            p.playlist.vinilos.forEach {
                                Text(
                                    text = "• $it",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { viewModel.toggleFavorita(p.playlist.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (p.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (p.esFavorita) "Quitar de Favoritos" else "Marcar como Favorita")
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    onClick = onBack,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text("Volver")
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    text = "Comentarios:",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            items(comentarios) { comentario ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF232323)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = comentario.username,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = comentario.texto,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                if (currentUser != null) {
                    OutlinedTextField(
                        value = nuevoComentario,
                        onValueChange = {
                            nuevoComentario = it
                            errorMessage = null
                        },
                        label = { Text("Añadir comentario", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF232323)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFFFF6B6B),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            if (nuevoComentario.isBlank()) {
                                errorMessage = "El comentario no puede estar vacío"
                                return@Button
                            }
                            scope.launch {
                                try {
                                    viewModel.insertComentario(
                                        Comentario(
                                            playlistId = id,
                                            userId = currentUser!!.id,
                                            texto = nuevoComentario
                                        )
                                    )
                                    nuevoComentario = ""
                                    errorMessage = null
                                } catch (e: Exception) {
                                    errorMessage = "Error al guardar el comentario: ${e.message}"
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Comentar", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(
                        text = "Inicia sesión para comentar.",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
