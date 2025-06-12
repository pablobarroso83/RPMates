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

    init {
        // Crear superusuario si no existe
        viewModelScope.launch {
            createSuperUserIfNeeded()
        }
    }

    private suspend fun createSuperUserIfNeeded() {
        try {
            val adminCount = repository.getAdminCount()
            if (adminCount == 0) {
                val superUser = User(
                    username = "admin",
                    password = PasswordUtils.hashPassword("admin123"),
                    email = "admin@rpmates.com",
                    firstName = "Super",
                    lastName = "Admin",
                    isAdmin = true
                )
                repository.insertUser(superUser)
            }
        } catch (e: Exception) {
            // Log error but don't crash the app
            e.printStackTrace()
        }
    }

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

    suspend fun register(
        username: String, 
        password: String,
        email: String = "",
        firstName: String = "",
        lastName: String = "",
        phoneNumber: String = "",
        dateOfBirth: String = ""
    ): Result<Unit> {
        return try {
            // Verificar si el usuario ya existe
            if (repository.getUserByUsername(username) != null) {
                return Result.failure(Exception("El usuario ya existe"))
            }
            
            // Verificar si el email ya existe
            if (email.isNotEmpty() && repository.getUserByEmail(email) != null) {
                return Result.failure(Exception("El email ya está registrado"))
            }
            
            val hashedPassword = PasswordUtils.hashPassword(password)
            val newUser = User(
                username = username,
                password = hashedPassword,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                dateOfBirth = dateOfBirth,
                isAdmin = false
            )
            val userId = repository.insertUser(newUser)
            _currentUser.value = newUser.copy(id = userId.toInt())
            Result.success(Unit)
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

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            repository.updateUser(user)
            _currentUser.value = user
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar usuario: ${e.message}"))
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