package com.example.veterinariav2.data

import com.example.veterinariav2.model.CitaList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/citas/mascota/{mascotaId}")
    suspend fun getCitasPorMascota(@Path("mascotaId") mascotaId: Int): Response<List<CitaList>>
}
