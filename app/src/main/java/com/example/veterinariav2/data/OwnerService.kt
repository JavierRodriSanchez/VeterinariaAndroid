package com.example.veterinariav2.data

import com.example.veterinariav2.model.OnwerPost
import com.example.veterinariav2.model.Owner
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OwnerService {
    @GET("api/dueños/{ownerId}")
    suspend fun getOwnerById(@Path("ownerId") ownerId: Int): Response<Owner>

    @GET("/api/dueños")
    suspend fun getOwners(): Response<List<Owner>>

    @POST("/api/dueños/add")
    suspend fun createOwner(@Body owner: OnwerPost): Response<OnwerPost>
}
