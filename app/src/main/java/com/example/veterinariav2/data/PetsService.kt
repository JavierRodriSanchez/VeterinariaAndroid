package com.example.veterinariav2.data

import com.example.veterinariav2.model.PetPost
import com.example.veterinariav2.model.Pets
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetsService {
    @GET("/mascotas")
    suspend fun getPets(): Response<List<Pets>>

    @POST("/mascotas/nueva")
    suspend fun createPet(@Body pet: PetPost): Response<Unit>
}
