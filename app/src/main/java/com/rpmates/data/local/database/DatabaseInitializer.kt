package com.rpmates.data.local.database

import com.rpmates.data.Repository
import com.rpmates.data.local.entities.Playlist
import com.rpmates.data.local.entities.User
import com.rpmates.util.PasswordUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/*
class DatabaseInitializer(private val repository: Repository) {

    fun initializeDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            // Crear usuario de prueba
            val user = User(
                username = "test",
                password = PasswordUtils.hashPassword("test123")
            )
            val userId = repository.insertUser(user)

            // Crear playlists de prueba
            val playlists = listOf(
                Playlist(
                    titulo = "Rock Clásico",
                    descripcion = "Los mejores temas de rock de todos los tiempos",
                    vinilos = listOf(
                        "Led Zeppelin - IV",
                        "Pink Floyd - Dark Side of the Moon",
                        "The Beatles - Abbey Road",
                        "The Rolling Stones - Exile on Main St.",
                        "The Who - Who's Next"
                    )
                ),
                Playlist(
                    titulo = "Jazz Essentials",
                    descripcion = "Colección esencial de jazz",
                    vinilos = listOf(
                        "Miles Davis - Kind of Blue",
                        "John Coltrane - A Love Supreme",
                        "Dave Brubeck - Time Out",
                        "Thelonious Monk - Brilliant Corners",
                        "Charles Mingus - Mingus Ah Um"
                    )
                ),
                Playlist(
                    titulo = "Hip Hop Classics",
                    descripcion = "Los clásicos del hip hop",
                    vinilos = listOf(
                        "Nas - Illmatic",
                        "Wu-Tang Clan - Enter the Wu-Tang",
                        "A Tribe Called Quest - The Low End Theory",
                        "Public Enemy - It Takes a Nation of Millions",
                        "Dr. Dre - The Chronic"
                    )
                ),
                Playlist(
                    titulo = "Soul & R&B",
                    descripcion = "Lo mejor del soul y R&B",
                    vinilos = listOf(
                        "Marvin Gaye - What's Going On",
                        "Stevie Wonder - Songs in the Key of Life",
                        "Aretha Franklin - Lady Soul",
                        "Curtis Mayfield - Superfly",
                        "Otis Redding - Otis Blue"
                    )
                ),
                Playlist(
                    titulo = "Electrónica",
                    descripcion = "Clásicos de la música electrónica",
                    vinilos = listOf(
                        "Daft Punk - Discovery",
                        "The Chemical Brothers - Dig Your Own Hole",
                        "Aphex Twin - Selected Ambient Works 85-92",
                        "Kraftwerk - Trans-Europe Express",
                        "Underworld - Dubnobasswithmyheadman"
                    )
                ),
                Playlist(
                    titulo = "Indie & Alternativo",
                    descripcion = "Lo mejor del indie y rock alternativo",
                    vinilos = listOf(
                        "Radiohead - OK Computer",
                        "Arcade Fire - Funeral",
                        "The Strokes - Is This It",
                        "Vampire Weekend - Modern Vampires of the City",
                        "Tame Impala - Currents"
                    )
                ),
                Playlist(
                    titulo = "Folk & Acústico",
                    descripcion = "Música folk y acústica esencial",
                    vinilos = listOf(
                        "Bob Dylan - Blood on the Tracks",
                        "Nick Drake - Pink Moon",
                        "Joni Mitchell - Blue",
                        "Fleet Foxes - Fleet Foxes",
                        "Bon Iver - For Emma, Forever Ago"
                    )
                )
            )

            // Insertar playlists
            playlists.forEach { playlist ->
                repository.insertPlaylist(playlist)
            }
        }
    }
} */