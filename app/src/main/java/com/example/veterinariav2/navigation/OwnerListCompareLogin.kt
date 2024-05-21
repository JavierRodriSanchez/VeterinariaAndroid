package com.example.veterinariav2.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.Owner

import kotlinx.coroutines.CoroutineExceptionHandler

@Composable
fun OwnersListScreen() {
    var owners by remember { mutableStateOf<List<Owner>>(emptyList()) }

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Handle exceptions here
        throwable.printStackTrace()
    }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.ownerService.getOwners()
            if (response.isSuccessful) {
                owners = response.body() ?: emptyList()
            } else {
                // Handle unsuccessful response
            }
        } catch (e: Exception) {
            // Handle network or other exceptions
            e.printStackTrace()
        }
    }

    LazyColumn {
        items(owners) { owner ->
            OwnerCard(owner = owner)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OwnerCard(owner: Owner) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nombre: ${owner.nombre}")
            Text(text = "Teléfono: ${owner.telefono}")
            Text(text = "Correo: ${owner.correo}")

            // Mostrar mascotas si están disponibles
            if (owner.mascota.isNotEmpty()) {
                Text(text = "Mascotas:")
                owner.mascota.forEach { pet ->
                    Text(text = "- ${pet.nombre}")
                }
            }

            Button(
                onClick = { /* Acción al hacer clic en el botón */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Pedir Cita")
            }
        }
    }
}
