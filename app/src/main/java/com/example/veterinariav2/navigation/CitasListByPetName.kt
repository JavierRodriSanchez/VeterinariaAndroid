package com.example.veterinariav2.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.CitaList

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
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
                    Text(
                        text = "Lista de Citas",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
            items(citasState.value) { cita ->
                CitasCard(cita = cita)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CitasCard(cita: CitaList) {
    val formattedDate = formatFechaCita(cita.fechaCita)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Fecha: $formattedDate")
            Text(text = "Motivo: ${cita.motivo}")
            Text(text = "Observaciones: ${cita.observaciones}")
            Text(text = "Nombre de la mascota: ${cita.nombre}")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatFechaCita(fechaCita: String): String {
    val dateTime = LocalDateTime.parse(fechaCita, DateTimeFormatter.ISO_DATE_TIME)
    val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm", Locale("es"))
    return dateTime.format(formatter)
}