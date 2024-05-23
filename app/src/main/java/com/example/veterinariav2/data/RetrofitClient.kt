package com.example.veterinariav2.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //
    val petsService: PetsService by lazy {
        retrofit.create(PetsService::class.java)
    }
    val ownerService: OwnerService by lazy {
        retrofit.create(OwnerService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val citaService: CitaService by lazy {
        retrofit.create(CitaService::class.java)
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val tipoMedicinaService: TipoMedicinaService by lazy {
        retrofit.create(TipoMedicinaService::class.java)
    }

    val medicineService: MedicineService by lazy {
        retrofit.create(MedicineService::class.java)
    }

    val pagosService :PagoService by lazy {
        retrofit.create(PagoService::class.java)
    }
}
