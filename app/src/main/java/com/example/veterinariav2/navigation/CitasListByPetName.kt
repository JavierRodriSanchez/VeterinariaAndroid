package com.example.veterinariav2.navigation

import android.annotation.SuppressLint


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.CitaList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasTextField(mascotaId: Int) {
    val citasState = remember { mutableStateOf<List<CitaList>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getCitasPorMascota(mascotaId)
            if (response.isSuccessful) {
                citasState.value = response.body() ?: emptyList()
            } else {
                // Manejar respuesta no exitosa
            }
        } catch (e: Exception) {
            // Manejar excepción
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                        Text(text = "Lista de Citas",textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                }
            )
        }
    ) {
        Spacer(modifier = Modifier.padding(50.dp))
        // Muestra la lista de citas en un  a LazyColumn con Cards
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
            items(citasState.value) { cita ->
                CitasCard(cita = cita)
            }
        }
    }
}

@Composable
fun CitasCard(cita: CitaList) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Fecha: ${cita.fechaCita}")
            Text(text = "Motivo: ${cita.motivo}")
            Text(text = "Observaciones: ${cita.observaciones}")
            Text(text = "Nombre de la mascota: ${cita.nombre}")
        }
    }
}
