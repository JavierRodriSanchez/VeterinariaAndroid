// En otro archivo, por ejemplo, en PetsScreen.kt

package com.example.veterinariav2.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.veterinariav2.R
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.Pets
import com.example.veterinariav2.ui.theme.Shapes

@Composable
fun PetsListScreen() {
    var pets by remember { mutableStateOf<List<Pets>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.petsService.getPets()
            if (response.isSuccessful) {
                pets = response.body() ?: emptyList()
            } else {
                // Handle unsuccessful response
            }
        } catch (e: Exception) {
            // Handle network or other exceptions
        }
    }

    LazyColumn {
        items(pets) { pet ->
            PetCard(pet = pet)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
fun PetCard(pet: Pets) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = Shapes.medium)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Columna para los datos
                Image(
                    painter = rememberImagePainter(
                        data = pet.imagen,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_background)
                        }
                    ),
                    contentDescription = "Imagen de la mascota",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape = Shapes.small),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Nombre: ${pet.nombre}")
                    Text(text = "Edad: ${pet.edad}")
                    Text(text = "Raza: ${pet.raza}")
                    Text(text = "Id: ${pet.id}")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* Acción al hacer clic en el botón */ },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp) // Padding uniforme
                ) {
                    Text(text = "Pedir Cita")
                }
            }
        }
    }
}
