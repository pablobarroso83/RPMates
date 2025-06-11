package com.rpmates.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rpmates.util.StringListConverter

@Entity(tableName = "playlists")
@TypeConverters(StringListConverter::class)
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val vinilos: List<String>
)
