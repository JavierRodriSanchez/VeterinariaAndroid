package com.example.veterinariav2.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.veterinariav2.R
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.Owner
import com.example.veterinariav2.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerListScreen(navController: NavController, ownerId: Int) {
    var owner by remember { mutableStateOf<Owner?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(ownerId) {
        try {
            val response = RetrofitClient.ownerService.getOwnerById(ownerId)
            if (response.isSuccessful) {
                owner = response.body()
            } else {
                errorMessage = "Error: ${response.code()} - ${response.message()}"
            }
        } catch (e: Exception) {
            errorMessage = "Exception: ${e.message}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mascotas",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { navController.navigate("login") }) {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.flechaizquierda),
                                contentDescription = "Login"
                            )

                        }
                        //-------------------------------------------------------------Cmabiar
                        Button(onClick = { navController.navigate("ListTipoMedicina/${ownerId}")}) {
                            Image(
                                modifier = Modifier.size(24.dp),
                                        painter = painterResource(id = R.drawable.carritocompras),
                                contentDescription = "Icono 1"
                            )



                        }

                        Button(onClick = { navController.navigate("mapa") }) {
                            Text("Mapa")
                        }
                        Button(onClick = { /* Acción para el cuarto botón */ }) {
                            Text("Botón 4")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (errorMessage != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = errorMessage ?: "Unknown error")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (owner != null) {
                    // Botón para ir a la pantalla de creación de mascotas
                    Button(
                        onClick = { navController.navigate("createPet/${owner?.id}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Añadir Nueva Mascota")
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(owner!!.mascota ?: emptyList()) { pet ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
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
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                    Button(
                                        onClick = { navController.navigate("createCita/${pet.id}/${owner?.id}") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "Pedir Cita")
                                    }

                                    //Posible borrado
                                    Button(
                                        onClick = { navController.navigate("CitasList/${pet.id}") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(text = "Ver citas")
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "Loading owner details...")
                }
            }
        }
    }
}
