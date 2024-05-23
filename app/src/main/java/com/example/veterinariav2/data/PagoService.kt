package com.example.veterinariav2.data

import com.example.veterinariav2.model.PagoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PagoService {

    @POST("api/paid/añadir")
    suspend fun enviarPago(@Body pago: PagoRequest): Response<Unit>
}