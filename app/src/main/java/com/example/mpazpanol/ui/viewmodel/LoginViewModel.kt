package com.example.mpazpanol.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mpazpanol.data.database.AppDatabase
import com.example.mpazpanol.model.LoginModel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginModel())
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email, emailError = null, errorGeneral = null)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password, passwordError = null, errorGeneral = null)
    }


    private fun validar(state: LoginModel): LoginModel {
        return state.copy(
            emailError = when {
                state.email.isBlank() -> "Debes ingresar tu correo electrónico."
                !Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() ->
                    "Ingresa un correo válido. Ej: nombre@correo.com"
                else -> null
            },
            passwordError = if (state.password.isBlank()) "Debes ingresar tu contraseña." else null
        )
    }


    fun onLoginClick(context: Context, onSuccess: () -> Unit) {
        val validado = validar(uiState)

        if (validado.emailError != null || validado.passwordError != null) {
            uiState = validado.copy(errorGeneral = null)
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val user = db.usuarioDao().getUserByEmail(validado.email.trim())

            if (user != null && user.password == validado.password) {
                uiState = validado.copy(errorGeneral = null)
                onSuccess()
            } else {
                uiState = validado.copy(errorGeneral = "Correo o contraseña incorrectos.")
            }
        }
    }
}