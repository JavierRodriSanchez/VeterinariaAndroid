package com.example.veterinariav2.model

import com.google.gson.annotations.SerializedName

data class Medicine(
    @SerializedName("idMedicina") val idMedicina: Int,
    @SerializedName("nombreMedicina") val nombreMedicina: String,
    @SerializedName("mascotaId") val mascotaId: Int,
    @SerializedName("nombreMascota") val nombreMascota: String,
    @SerializedName("tipoId") val tipoId: Int,
    @SerializedName("nombreTipo") val nombreTipo: String,
    @SerializedName("precio") val precio: Double
)
