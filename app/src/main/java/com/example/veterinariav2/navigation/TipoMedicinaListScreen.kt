package com.example.veterinariav2.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.veterinariav2.R
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.TipoMedicina
import com.example.veterinariav2.ui.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoMedicinaListScreen(navController: NavController) {
    var tiposMedicina by remember { mutableStateOf<List<TipoMedicina>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            tiposMedicina = RetrofitClient.tipoMedicinaService.getTiposMedicina()
        } catch (e: Exception) {
            // Handle network or other exceptions
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.logoperro), // Reemplaza con tu imagen
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(100.dp) // Tamaño de la imagen aumentado
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "Productos",
                                fontSize = 24.sp, // Tamaño del texto aumentado
                                fontWeight = FontWeight.Bold // Negrita para hacer el texto más destacado
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                items(tiposMedicina) { tipoMedicina ->
                    TipoMedicinaCard(tipoMedicina = tipoMedicina, navController = navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}

@Composable
fun TipoMedicinaCard(tipoMedicina: TipoMedicina, navController: NavController) {


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
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "${tipoMedicina.nombreTipo}", textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.width(16.dp))
                MyButton(
                    onClick = { navController.navigate("ListMedicines/${tipoMedicina.idTipo}") },
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp) // Padding uniforme
                )
            }
        }
    }
}

@Composable
fun MyButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Arrow Forward")
    }
}
