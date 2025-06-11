package com.rpmates.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rpmates.data.local.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?
}
