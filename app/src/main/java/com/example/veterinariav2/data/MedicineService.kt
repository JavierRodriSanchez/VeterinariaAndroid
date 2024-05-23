package com.example.veterinariav2.data

import com.example.veterinariav2.model.Medicine
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MedicineService {
    @GET("/api/medicines/tipo/{tipoId}")
    suspend fun getMedicinesByType(@Path("tipoId") tipoId: Int): Response<List<Medicine>>
}
