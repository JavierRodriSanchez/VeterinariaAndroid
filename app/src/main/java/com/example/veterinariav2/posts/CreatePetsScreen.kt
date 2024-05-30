package com.example.veterinariav2.posts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.PetPost
import com.example.veterinariav2.model.User
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePetScreen(navController: NavController, ownerId: Int?) {
    var nombre by remember { mutableStateOf("") }
    var idVeterinario by remember { mutableStateOf("") }
    val idDueno = ownerId?.toString() ?: ""
    var edad by remember { mutableStateOf(0f) }
    var raza by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var expandedSexo by remember { mutableStateOf(false) }
    var peso by remember { mutableStateOf(0f) }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var message by remember { mutableStateOf("") }

    var veterinarios by remember { mutableStateOf<List<User>>(emptyList()) }
    var selectedVeterinario by remember { mutableStateOf<User?>(null) }
    var expandedVeterinario by remember { mutableStateOf(false) }

    val isFormValid = nombre.isNotBlank() && idVeterinario.isNotBlank() && idDueno.isNotBlank() && edad > 0 && raza.isNotBlank() && sexo.isNotBlank() && peso > 0 && imagenUri != null
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.userService.getUsers()
                if (response.isSuccessful) {
                    veterinarios = response.body() ?: emptyList()
                } else {
                    message = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                message = "Exception: ${e.message}"
            }
        }
    }

    // Launcher para seleccionar la imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imagenUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it.trim() },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Exposed Dropdown Menu for Veterinarios
        ExposedDropdownMenuBox(
            expanded = expandedVeterinario,
            onExpandedChange = { expandedVeterinario = !expandedVeterinario }
        ) {
            TextField(
                value = selectedVeterinario?.nombre ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Veterinario") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVeterinario)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedVeterinario,
                onDismissRequest = { expandedVeterinario = false }
            ) {
                veterinarios.forEach { veterinario ->
                    DropdownMenuItem(
                        text = { Text(veterinario.nombre) },
                        onClick = {
                            selectedVeterinario = veterinario
                            idVeterinario = veterinario.id.toString()
                            expandedVeterinario = false
                        }
                    )
                }
            }
        }

        TextField(
            value = idDueno,
            onValueChange = { /* no permitir cambios ya que viene del ID del dueño */ },
            label = { Text("ID Dueño") },
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Selector de edad
        Text(text = "Edad: ${edad.toInt()} años")
        Slider(
            value = edad,
            onValueChange = { edad = it },
            valueRange = 0f..20f,
            steps = 19,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = raza,
            onValueChange = { raza = it.trim() },
            label = { Text("Raza") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Exposed Dropdown Menu for Sexo
        ExposedDropdownMenuBox(
            expanded = expandedSexo,
            onExpandedChange = { expandedSexo = !expandedSexo }
        ) {
            TextField(
                value = sexo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSexo)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedSexo,
                onDismissRequest = { expandedSexo = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Hembra") },
                    onClick = {
                        sexo = "Hembra"
                        expandedSexo = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Macho") },
                    onClick = {
                        sexo = "Macho"
                        expandedSexo = false
                    }
                )
            }
        }

        // Selector de peso
        Text(text = "Peso: ${peso.toInt()} kg")
        Slider(
            value = peso,
            onValueChange = { peso = it },
            valueRange = 0f..50f,
            steps = 49,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Botón para seleccionar la imagen
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Seleccionar Imagen")
        }

        imagenUri?.let {
            Text("Imagen seleccionada: $it")
        }

        Button(
            onClick = {
                val pet = PetPost(
                    nombre = nombre,
                    idVeterinario = idVeterinario.toInt(),
                    idDueno = idDueno.toInt(),
                    edad = edad.toInt(),
                    raza = raza,
                    sexo = sexo,
                    peso = peso.toInt().toString(), // Convertir el peso a cadena
                    imagen = imagenUri.toString()
                )
                scope.launch {
                    try {
                        val response = RetrofitClient.petsService.createPet(pet)
                        if (response.isSuccessful) {
                            message = "Pet created successfully!"
                            navController.popBackStack()// Navigate back to the login screen
                        } else {
                            message = "Error: ${response.code()} - ${response.message()}"
                        }
                    } catch (e: Exception) {
                        message = "Exception: ${e.message}"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = isFormValid // Habilitar o deshabilitar el botón basado en la validación del formulario
        ) {
            Text("Create Pet")
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al inicio
        Button(
            onClick = {
                navController.popBackStack() // Navigate back to the previous destination
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Volver al inicio")
        }
    }
}


