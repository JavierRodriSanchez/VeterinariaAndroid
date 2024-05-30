package com.example.veterinariav2.model

import com.google.gson.annotations.SerializedName

data class TipoMedicina(
    @SerializedName("idTipo") val idTipo: Int,
    @SerializedName("nombreTipo") val nombreTipo: String,
    @SerializedName("precio") val precio: Double
)
