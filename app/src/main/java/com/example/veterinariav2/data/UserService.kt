// UserService.kt
package com.example.veterinariav2.data

import com.example.veterinariav2.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("/api/vets/findAll")  // Reemplaza con el endpoint correcto
    suspend fun getUsers(): Response<List<User>>
}
