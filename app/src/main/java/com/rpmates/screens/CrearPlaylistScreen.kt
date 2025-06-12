package com.rpmates.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpmates.data.local.entities.Playlist
import com.rpmates.viewmodel.PlayListViewModel

@Composable
fun CrearPlaylistScreen(
    onBack: () -> Unit,
    viewModel: PlayListViewModel
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var vinilos by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    // Validaciones
    val isTituloValid = titulo.isNotBlank()
    val isDescripcionValid = descripcion.isNotBlank()
    val isVinilosValid = vinilos.isNotBlank()
    val isFormValid = isTituloValid && isDescripcionValid && isVinilosValid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Crear Playlist",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2D2D2D)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = {
                            titulo = it
                            errorMessage = null
                        },
                        label = { Text("Título *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isTituloValid && titulo.isNotEmpty(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        supportingText = {
                            if (!isTituloValid && titulo.isNotEmpty()) {
                                Text("El título es obligatorio", color = Color(0xFFFF6B6B))
                            }
                        }
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                            errorMessage = null
                        },
                        label = { Text("Descripción *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isDescripcionValid && descripcion.isNotEmpty(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        supportingText = {
                            if (!isDescripcionValid && descripcion.isNotEmpty()) {
                                Text("La descripción es obligatoria", color = Color(0xFFFF6B6B))
                            }
                        }
                    )

                    OutlinedTextField(
                        value = genre,
                        onValueChange = { genre = it },
                        label = { Text("Género", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Rock, Jazz, Hip Hop, etc.", color = Color.Gray) },
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

                    OutlinedTextField(
                        value = vinilos,
                        onValueChange = {
                            vinilos = it
                            errorMessage = null
                        },
                        label = { Text("Vinilos (separados por coma) *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isVinilosValid && vinilos.isNotEmpty(),
                        placeholder = { Text("Led Zeppelin - IV, Pink Floyd - DSOTM", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        supportingText = {
                            if (!isVinilosValid && vinilos.isNotEmpty()) {
                                Text("Debes agregar al menos un vinilo", color = Color(0xFFFF6B6B))
                            }
                        }
                    )

                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text("Etiquetas (separadas por coma)", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("clásico, vintage, colección", color = Color.Gray) },
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Playlist pública",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = isPublic,
                            onCheckedChange = { isPublic = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4CAF50),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF424242)
                            )
                        )
                    }

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFFFF6B6B),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (!isFormValid) {
                                    errorMessage = "Por favor, completa todos los campos obligatorios"
                                    return@Button
                                }
                                val listaVinilos = vinilos.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                val listaTags = if (tags.isNotEmpty()) {
                                    tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                } else {
                                    emptyList()
                                }
                                
                                viewModel.insertPlaylist(
                                    Playlist(
                                        titulo = titulo,
                                        descripcion = descripcion,
                                        vinilos = listaVinilos,
                                        genre = genre,
                                        tags = listaTags,
                                        isPublic = isPublic
                                    )
                                )
                                onBack()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            enabled = isFormValid,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Guardar", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onBack,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Cancelar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}