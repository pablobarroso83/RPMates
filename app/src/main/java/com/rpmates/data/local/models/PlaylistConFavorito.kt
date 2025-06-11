package com.rpmates.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.rpmates.data.local.entities.Playlist

data class PlaylistConFavorito(
    @Embedded val playlist: Playlist,
    val esFavorita: Boolean
)