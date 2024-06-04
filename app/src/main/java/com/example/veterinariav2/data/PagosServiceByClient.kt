package com.example.veterinariav2.data

import com.example.veterinariav2.model.Medicine
import com.example.veterinariav2.model.Pagos
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path


interface PagosServiceByClient {

    @GET("api/paid/listaPagos/{ownerId}")
    suspend fun getPagos(@Path("ownerId") ownerId: Int): List<Pagos>
}