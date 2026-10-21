package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.data.local.entity.UsuarioEntity
import com.example.mpazpanol.model.RegisterModel
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    var uiState by mutableStateOf(RegisterModel())
        private set


    fun onNameChanged(name: String) {
        uiState = uiState.copy(name = name, nameError = null)
    }

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email, emailError = null)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password, passwordError = null, confirmPasswordError = null)
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        uiState = uiState.copy(confirmPassword = confirmPassword, confirmPasswordError = null)
    }


    private fun validar(state: RegisterModel): RegisterModel {
        return state.copy(
            nameError = when {
                state.name.isBlank() -> "Debes ingresar tu nombre."
                state.name.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres."
                else -> null
            },
            emailError = when {
                state.email.isBlank() -> "Debes ingresar tu correo electrónico."
                !Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() ->
                    "Ingresa un correo válido. Ej: nombre@correo.com"
                else -> null
            },
            passwordError = when {
                state.password.isBlank() -> "Debes ingresar una contraseña."
                state.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
                state.password.none { it.isDigit() } -> "La contraseña debe incluir al menos un número."
                else -> null
            },
            confirmPasswordError = when {
                state.confirmPassword.isBlank() -> "Debes confirmar tu contraseña."
                state.password != state.confirmPassword -> "Las contraseñas no coinciden."
                else -> null
            }
        )
    }

    private fun tieneErrores(s: RegisterModel): Boolean =
        s.nameError != null || s.emailError != null ||
                s.passwordError != null || s.confirmPasswordError != null


    fun onRegisterClick(context: Context, onSuccess: () -> Unit) {
        val validado = validar(uiState)

        if (tieneErrores(validado)) {
            uiState = validado
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val existente = db.usuarioDao().getUserByEmail(validado.email.trim())

            if (existente != null) {
                uiState = validado.copy(emailError = "El correo ya está registrado.")
            } else {
                db.usuarioDao().registerUser(
                    UsuarioEntity(
                        name = validado.name.trim(),
                        email = validado.email.trim(),
                        password = validado.password
                    )
                )
                onSuccess()
            }
        }
    }
}