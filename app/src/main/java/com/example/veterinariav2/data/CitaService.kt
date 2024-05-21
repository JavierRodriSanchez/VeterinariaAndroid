package com.example.veterinariav2.data

import com.example.veterinariav2.model.Cita
import com.example.veterinariav2.model.CitaList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CitaService {
    @POST("/api/citas")
    suspend fun createCita(@Body cita: Cita): Response<Cita>


}
