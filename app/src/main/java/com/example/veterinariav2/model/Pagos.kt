package com.example.veterinariav2.model

import com.google.gson.annotations.SerializedName

data class Pagos(
    @SerializedName("idPago")
    val idPago: Int,
    @SerializedName("fechaPago")
    val fechaPago: String,
    @SerializedName("monto")
    val monto: Double
)