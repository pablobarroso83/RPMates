package com.rpmates.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rpmates.data.local.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE isAdmin = 1")
    suspend fun getAdminUsers(): List<User>

    @Query("SELECT COUNT(*) FROM users WHERE isAdmin = 1")
    suspend fun getAdminCount(): Int
}