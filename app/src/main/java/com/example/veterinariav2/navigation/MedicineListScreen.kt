    package com.example.veterinariav2.navigation


    import android.app.Activity
    import android.content.Context
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.ArrowForward
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavController
    import com.example.veterinariav2.R
    import com.example.veterinariav2.data.RetrofitClient
    import com.example.veterinariav2.model.Cita
    import com.example.veterinariav2.model.Medicine
    import com.example.veterinariav2.model.PagoRequest
    import com.google.android.gms.wallet.*
    import kotlinx.coroutines.launch
    import org.json.JSONArray
    import org.json.JSONObject
    import java.text.SimpleDateFormat
    import java.util.*

    @Composable
    fun MedicineListScreen(navController: NavController, tipoId: Int,ownerId:Int) {
        var medicines by remember { mutableStateOf<List<Medicine>>(emptyList()) }
        var totalValue by remember { mutableStateOf(0.0) } // Estado para mantener el valor total
        var showConfirmationDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            try {
                val response = RetrofitClient.medicineService.getMedicinesByType(tipoId)
                if (response.isSuccessful) {
                    medicines = response.body() ?: emptyList()
                } else {
                    // Manejar respuesta no exitosa
                }
            } catch (e: Exception) {
                // Manejar excepción
            }
        }

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo
            Image(
                painter = painterResource(id = R.drawable.logoperro), // Cambia por tu imagen
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f) // Para que la lista ocupe el espacio disponible
            ) {
                items(medicines) { medicine ->
                    MedicineCard(medicine = medicine, onAdd = { value ->
                        totalValue += value
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Barra inferior con los botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween // Espacio entre los botones
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Button(onClick = {
                    val fechaPago = obtenerFechaHoy()

                    // Crear un objeto PagoRequest con los datos necesarios
                    val pagoRequest = PagoRequest(idPago = 0, fechaPago = fechaPago, monto = totalValue, idMascota = ownerId) // Reemplaza 1 con el ID del owner  seleccionada

                    // Ejecutar la corrutina para enviar el pago
                    coroutineScope.launch {
                        try {
                            val response = RetrofitClient.pagosService.enviarPago(pagoRequest)
                            if (response.isSuccessful) {
                                // Mostrar diálogo de confirmación en caso de éxito
                                showConfirmationDialog = true
                            } else {
                                // Manejar respuesta no exitosa
                                // Por ejemplo, mostrar un mensaje de error
                            }
                        } catch (e: Exception) {
                            // Manejar excepción
                            // Por ejemplo, mostrar un mensaje de error
                        }
                    }
                }) {
                    Text(text = "Pagar: $totalValue")
                }
            }
        }

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                confirmButton = {
                    TextButton(onClick = { showConfirmationDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Pago Exitoso") },
                text = { Text("El pago se ha realizado correctamente.") }
            )
        }
    }}

    fun obtenerFechaHoy(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaHoy = Calendar.getInstance().time
        return dateFormat.format(fechaHoy)
    }

    @Composable
    fun MedicineCard(medicine: Medicine, onAdd: (Double) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Centrar horizontalmente los elementos de la columna
            ) {
                Text(text = "Nombre: ${medicine.nombreMedicina}", textAlign = TextAlign.Center)
                Text(text = "Precio: ${medicine.precio}", textAlign = TextAlign.Center)


                IconButton(onClick = { onAdd(medicine.precio) }) {
                    Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Add")
                }
            }
        }
    }
