package com.example.veterinariav2.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.OnwerPost
import com.example.veterinariav2.utils.Encyptado
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOwnerScreen(navController: NavController,email:String,password:String) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf(email) }
    var contrasena by remember { mutableStateOf(password) }
    var message by remember { mutableStateOf("") }

    // Variable para verificar si todos los campos están completos
    val isFormValid = nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && correo.isNotBlank() && contrasena.isNotBlank()

    val scope = rememberCoroutineScope()

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
        TextField(
            value = telefono,
            onValueChange = { telefono = it.trim() },
            label = { Text("Teléfono") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        TextField(
            value = direccion,
            onValueChange = { direccion = it.trim() },
            label = { Text("Dirección") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        TextField(
            value = correo,
            onValueChange = { correo = it.trim() },
            label = { Text("Correo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it.trim() },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                val owner = OnwerPost(
                    id = 0, // Assuming id is auto-generated on the server side
                    nombre = nombre,
                    telefono = telefono,
                    direccion = direccion,
                    correo = correo,
                    contrasena = Encyptado.devuelveEncryptado(contrasena)
                )
                scope.launch {
                    try {
                        val response = RetrofitClient.ownerService.createOwner(owner)
                        if (response.isSuccessful) {
                            message = "Owner created successfully!"
                            navController.navigate("login") // Navigate back to the login screen
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
            Text("Create Owner")
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
