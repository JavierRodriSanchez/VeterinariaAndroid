package com.example.veterinariav2.model

data class Role(
    val idRol: Int,
    val nombre: String
)

data class User(
    val id: Int,
    val apellidos: String,
    val codColegiado: String,
    val contrasena: String,
    val correo: String,
    val nombre: String,
    val telefono: String,
    val roles: Role
)
