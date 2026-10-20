package com.example.mpazpanol.ui.viewmodel

import android.content.Context
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
    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email)
        errorMessage = null
    }
    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password)
        errorMessage = null
    }
    fun onLoginClick(context: Context, onSuccess: () -> Unit) {
        val email = uiState.email
        val password = uiState.password

        if (email.isBlank()) {
            errorMessage = "Debes ingresar tu correo electrónico."
            return
        }

        if (password.isBlank()) {
            errorMessage = "Debes ingresar tu contraseña."
            return
        }


        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val user = db.usuarioDao().getUserByEmail(email)

            if (user != null && user.password == password) {
                errorMessage = null
                onSuccess()
            } else {
                errorMessage = "Correo o contraseña incorrectos."
            }
        }
    }
}