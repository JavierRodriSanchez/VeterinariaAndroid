package com.example.veterinariav2.data


import com.example.veterinariav2.model.TipoMedicina

import retrofit2.http.GET

interface TipoMedicinaService {
    @GET("/api/tipo/findAll")
    suspend fun getTiposMedicina(): List<TipoMedicina>
}