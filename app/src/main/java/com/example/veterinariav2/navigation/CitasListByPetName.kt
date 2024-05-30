package com.example.veterinariav2.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.CitaList
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasTextField(navController: NavController, mascotaId: Int) {
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
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.padding(50.dp))
            LazyColumn(modifier = Modifier.weight(1f).padding(top = 50.dp)) {
                items(citasState.value) { cita ->
                    CitasCard(cita = cita)
                }
            }
            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("← Volver")
            }
        }
    }
}

@Composable
fun CitasCard(cita: CitaList) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val formattedDate = OffsetDateTime.parse(cita.fechaCita).toLocalDateTime().format(formatter)

    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Fecha: $formattedDate")
            Text(text = "Motivo: ${cita.motivo}")
            Text(text = "Observaciones: ${cita.observaciones}")
        }
    }
}
