package com.example.mpazpanol.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("R e g i s t r o", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onNameChanged(it) },
                label = { Text("Nombre Completo") },
                singleLine = true,
                isError = state.errorMessage != null && state.name.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.errorMessage != null && state.name.isBlank()) {
                Text(
                    text = "Debes ingresar tu nombre.",
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = state.errorMessage != null &&
                        state.name.isNotBlank() &&
                        state.email.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.errorMessage != null &&
                state.name.isNotBlank() &&
                state.email.isBlank()
            ) {
                Text(
                    text = "Debes ingresar tu correo electrónico.",
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    viewModel.onPasswordChanged(it)
                },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = state.errorMessage != null &&
                        state.name.isNotBlank() &&
                        state.email.isNotBlank() &&
                        state.password.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.errorMessage != null &&
                state.name.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isBlank()
            ) {
                Text(
                    text = "Debes ingresar una contraseña.",
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = { Text("Repetir Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = {
                    viewModel.onConfirmPasswordChanged(it)
                },
                label = { Text("Confirmar Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = state.errorMessage != null &&
                        state.name.isNotBlank() &&
                        state.email.isNotBlank() &&
                        state.password.isNotBlank() &&
                        (state.confirmPassword.isBlank() ||
                                state.password != state.confirmPassword),
                modifier = Modifier.fillMaxWidth()
            )

            if (state.errorMessage != null &&
                state.name.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                state.confirmPassword.isBlank()
            ) {
                Text(
                    text = "Debes confirmar tu contraseña.",
                    color = Color.Red
                )
            }

            if (state.errorMessage != null &&
                state.confirmPassword.isNotBlank() &&
                state.password != state.confirmPassword
            ) {
                Text(
                    text = "Las contraseñas no coinciden.",
                    color = Color.Red
                )
            }

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