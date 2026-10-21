package com.example.mpazpanol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpazpanol.ui.components.CampoFormulario
import com.example.mpazpanol.ui.components.Footer
import com.example.mpazpanol.ui.components.Header
import com.example.mpazpanol.ui.viewmodel.RegisterViewModel

@Composable
fun Register(
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    Scaffold(
        topBar = { Header("REGISTRO DE USUARIO") },
        bottomBar = { Footer() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("R e g i s t r o", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            CampoFormulario(
                value = state.name,
                onValueChange = { viewModel.onNameChanged(it) },
                label = "Nombre Completo",
                placeholder = "Ej: Francisca Orrego",
                icono = Icons.Default.Person,
                error = state.nameError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoFormulario(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Correo Electrónico",
                placeholder = "nombre@correo.com",
                icono = Icons.Default.Email,
                error = state.emailError,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoFormulario(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Contraseña",
                placeholder = "Mínimo 6 caracteres y un número",
                icono = Icons.Default.Lock,
                error = state.passwordError,
                keyboardType = KeyboardType.Password,
                esPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CampoFormulario(
                value = state.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = "Confirmar Contraseña",
                icono = Icons.Default.Lock,
                error = state.confirmPasswordError,
                keyboardType = KeyboardType.Password,
                esPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.onRegisterClick(context) {
                        Toast.makeText(
                            context,
                            "¡Registro exitoso! Por favor inicia sesión.",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateToLogin()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4))
            ) {
                Text(text = "Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    Register(
        onNavigateToLogin = {}
    )
}