package com.example.veterinariav2.model

data class Cita(
    val idCita: Int,
    val fechaCita: String,
    val motivo: String,
    val observaciones: String,
    val mascota: Int
)