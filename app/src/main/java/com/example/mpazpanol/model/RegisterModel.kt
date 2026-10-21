package com.example.mpazpanol.model

data class RegisterModel(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)