package com.example.mpazpanol.model

data class LoginModel(
    val email: String = "",
    val password: String = "",

    val emailError: String? = null,
    val passwordError: String? = null,


    val errorGeneral: String? = null
)