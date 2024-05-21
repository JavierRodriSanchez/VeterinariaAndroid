package com.example.veterinariav2.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime



data class CitaList(
    @SerializedName("idCita") val idCita: Int,
    @SerializedName("fechaCita") val fechaCita: String,
    @SerializedName("motivo") val motivo: String,
    @SerializedName("observaciones") val observaciones: String,
    @SerializedName("idMascota") val idMascota: Int,
    @SerializedName("nombre") val nombre: String
)

