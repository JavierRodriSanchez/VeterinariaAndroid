package com.example.veterinariav2.model

data class PagoRequest(
    val idPago: Int,
    val fechaPago: String,
    val monto: Double,
    val idMascota: Int // ID de la mascota asociada al pago
)
