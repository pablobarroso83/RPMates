package com.rpmates.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpmates.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: UserViewModel
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Validaciones
    val isUsernameValid = username.length >= 3
    val isEmailValid = email.contains("@") && email.contains(".")
    val isPasswordValid = password.length >= 6
    val doPasswordsMatch = password == confirmPassword
    val isFormValid = isUsernameValid && isEmailValid && isPasswordValid && 
                     doPasswordsMatch && firstName.isNotBlank() && lastName.isNotBlank()

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
                text = "Crear cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
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
                    // Información básica
                    Text(
                        text = "Información personal",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { 
                                firstName = it
                                errorMessage = null
                            },
                            label = { Text("Nombre *", color = Color.White) },
                            modifier = Modifier.weight(1f),
                            isError = firstName.isBlank() && errorMessage != null,
                            enabled = !isLoading,
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
                            value = lastName,
                            onValueChange = { 
                                lastName = it
                                errorMessage = null
                            },
                            label = { Text("Apellidos *", color = Color.White) },
                            modifier = Modifier.weight(1f),
                            isError = lastName.isBlank() && errorMessage != null,
                            enabled = !isLoading,
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
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            errorMessage = null
                        },
                        label = { Text("Email *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isEmailValid && email.isNotEmpty(),
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                            if (!isEmailValid && email.isNotEmpty()) {
                                Text("Email no válido", color = Color(0xFFFF6B6B))
                            }
                        }
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Teléfono", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = { Text("Fecha de nacimiento (DD/MM/AAAA)", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        placeholder = { Text("01/01/1990", color = Color.Gray) },
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

                    Divider(color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))

                    // Información de cuenta
                    Text(
                        text = "Información de cuenta",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = { 
                            username = it
                            errorMessage = null
                        },
                        label = { Text("Usuario *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isUsernameValid && username.isNotEmpty(),
                        enabled = !isLoading,
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
                            if (!isUsernameValid && username.isNotEmpty()) {
                                Text("Mínimo 3 caracteres", color = Color(0xFFFF6B6B))
                            }
                        }
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            errorMessage = null
                        },
                        label = { Text("Contraseña *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !isPasswordValid && password.isNotEmpty(),
                        enabled = !isLoading,
                        visualTransformation = PasswordVisualTransformation(),
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
                            if (!isPasswordValid && password.isNotEmpty()) {
                                Text("Mínimo 6 caracteres", color = Color(0xFFFF6B6B))
                            }
                        }
                    )
                
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { 
                            confirmPassword = it
                            errorMessage = null
                        },
                        label = { Text("Confirmar contraseña *", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = !doPasswordsMatch && confirmPassword.isNotEmpty(),
                        enabled = !isLoading,
                        visualTransformation = PasswordVisualTransformation(),
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
                            if (!doPasswordsMatch && confirmPassword.isNotEmpty()) {
                                Text("Las contraseñas no coinciden", color = Color(0xFFFF6B6B))
                            }
                        }
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
                            if (!isFormValid) {
                                errorMessage = "Por favor, completa todos los campos obligatorios correctamente"
                                return@Button
                            }
                            
                            scope.launch {
                                try {
                                    isLoading = true
                                    errorMessage = null
                                    viewModel.register(
                                        username = username,
                                        password = password,
                                        email = email,
                                        firstName = firstName,
                                        lastName = lastName,
                                        phoneNumber = phoneNumber,
                                        dateOfBirth = dateOfBirth
                                    ).fold(
                                        onSuccess = {
                                            onRegisterSuccess()
                                        },
                                        onFailure = { error ->
                                            errorMessage = error.message
                                        }
                                    )
                                } catch (e: Exception) {
                                    errorMessage = "Error inesperado: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isLoading && isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                "Registrarse",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(
                            "¿Ya tienes cuenta? Inicia sesión",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}