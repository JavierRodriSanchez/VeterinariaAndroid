package com.example.veterinariav2.model

data class PetPost(
    val nombre: String,
    val idVeterinario: Int,
    val idDueno: Int,
    val edad: Int,
    val raza: String,
    val sexo: String,
    val peso: String,
    val imagen: String
)
