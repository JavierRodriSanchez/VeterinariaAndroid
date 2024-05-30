package com.example.veterinariav2.posts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.Cita
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCitaScreen(navController: NavController, petId: Int?, ownerId: Int?) {
    var fechaCita by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Date and Time Pickers
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val time = String.format("%02d:%02d", hourOfDay, minute)
                    val dateTimeString = "$date $time"
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
                    val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    fechaCita = localDateTime.format(isoFormatter)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Date and Time Picker Button
        OutlinedButton(
            onClick = { datePickerDialog.show() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(if (fechaCita.isEmpty()) "Seleccionar Fecha y Hora" else fechaCita)
        }

        // Motivo Text Field
        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it.trim() },
            label = { Text("Motivo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Observaciones Text Field
        OutlinedTextField(
            value = observaciones,
            onValueChange = { observaciones = it.trim() },
            label = { Text("Observaciones") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Create Cita Button
        Button(
            onClick = {
                val cita = Cita(
                    idCita = 0,
                    fechaCita = fechaCita,
                    motivo = motivo,
                    observaciones = observaciones,
                    mascota = petId ?: 0 // Usar petId aquí
                )
                scope.launch {
                    try {
                        // Obtener todas las citas existentes
                        val listCitas = RetrofitClient.apiService.getCitasAll().body() ?: emptyList()
                        val newCitaDateTime = LocalDateTime.parse(fechaCita, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))

                        // Verificar si hay un conflicto con las citas existentes
                        val conflict = listCitas.any { existingCita ->
                            val existingCitaDateTime = LocalDateTime.parse(existingCita.fechaCita, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))
                            val isSameDay = existingCitaDateTime.toLocalDate().isEqual(newCitaDateTime.toLocalDate())
                            val isConflictingTime = newCitaDateTime.isAfter(existingCitaDateTime.minusMinutes(60)) && newCitaDateTime.isBefore(existingCitaDateTime.plusMinutes(60))
                            isSameDay && isConflictingTime
                        }

                        if (conflict) {
                            message = "Ya hay una cita en el rango de tiempo seleccionado."
                        } else {
                            val response = RetrofitClient.citaService.createCita(cita)
                            if (response.isSuccessful) {
                                message = "Cita creada exitosamente!"
                                navController.navigate("petsList/${ownerId}")
                            } else {
                                message = "Error: ${response.code()} - ${response.message()}"
                            }
                        }
                    } catch (e: Exception) {
                        message = "Excepción: ${e.message}"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Crear Cita")
        }

        // Display Message Text
        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
