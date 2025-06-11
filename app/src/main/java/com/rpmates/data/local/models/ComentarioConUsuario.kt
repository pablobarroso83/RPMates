package com.rpmates.data.local.models

data class ComentarioConUsuario(
    val id: Int,
    val playlistId: Int,
    val userId: Int,
    val texto: String,
    val username: String
)