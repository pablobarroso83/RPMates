package com.rpmates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rpmates.data.Repository
import com.rpmates.data.local.entities.User
import com.rpmates.util.PasswordUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val user = repository.getUserByUsername(username)
            when {
                user == null -> Result.failure(Exception("Usuario no encontrado"))
                !PasswordUtils.verifyPassword(password, user.password) -> 
                    Result.failure(Exception("Contraseña incorrecta"))
                else -> {
                    _currentUser.value = user
                    Result.success(Unit)
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión: ${e.message}"))
        }
    }

    suspend fun register(username: String, password: String): Result<Unit> {
        return try {
            if (repository.getUserByUsername(username) != null) {
                Result.failure(Exception("El usuario ya existe"))
            } else {
                val hashedPassword = PasswordUtils.hashPassword(password)
                val newUser = User(
                    username = username,
                    password = hashedPassword // Guardamos la contraseña hasheada
                )
                val userId = repository.insertUser(newUser)
                _currentUser.value = newUser.copy(id = userId.toInt())
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar usuario: ${e.message}"))
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun refreshUser() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                repository.getUserById(user.id)?.let { refreshedUser ->
                    _currentUser.value = refreshedUser
                }
            }
        }
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}