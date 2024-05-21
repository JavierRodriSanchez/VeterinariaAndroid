package com.example.veterinariav2.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veterinariav2.R
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.model.Owner
import com.example.veterinariav2.utils.Encyptado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    var owners by remember { mutableStateOf<List<Owner>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Obtener la lista de propietarios utilizando Retrofit
        owners = RetrofitClient.ownerService.getOwners().body() ?: emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoperro), // Replace R.drawable.your_image with your image resource
            contentDescription = "Logo",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Mail") },
            modifier = Modifier.padding(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.padding(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = PasswordVisualTransformation(),
            isError = password.isEmpty(),
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = BottomEnd // Alinea el contenido en la esquina inferior derecha
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val user = owners.find { it.correo == username && it.contrasena == Encyptado.devuelveEncryptado(password) }
                        if (user != null) {
                            print(user.id)
                            message = "Login successful"
                            navController.navigate("petsList/${user.id}") // Pasa el ID del propietario como Int
                        } else {
                            message = "Login failed: Invalid username or password"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp)
                ) {
                    Text("Login")
                }
                Spacer(modifier = Modifier.padding(bottom = 150.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "No tienes cuenta pulsa aquí:", modifier = Modifier.padding(bottom = 27.dp))
                    Button(
                        onClick = {
                            navController.navigate("createOwner")
                        },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Registrarse")
                    }
              }
               
            }
        }

        Text(
            text = message,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
