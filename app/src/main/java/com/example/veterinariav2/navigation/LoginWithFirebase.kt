package com.example.veterinariav2.navigation
import android.service.autofill.OnClickAction
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.veterinariav2.R
import com.example.veterinariav2.data.RetrofitClient
import com.example.veterinariav2.firebase.LoginViewModel
import com.example.veterinariav2.model.Owner
import com.example.veterinariav2.utils.Encyptado
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginVeterinariaScreen(
    navController: NavController,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {



    val showLoginForm = rememberSaveable { mutableStateOf(true) }
    var owners by remember { mutableStateOf<List<Owner>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Obtener la lista de propietarios utilizando Retrofit
        owners = RetrofitClient.ownerService.getOwners().body() ?: emptyList()
    }
    val token = "542517643352-l47uoj96al5dogptven9q6pk54nh7o4e.apps.googleusercontent.com"
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                val email = account.email
                if (email != null) {
                    val user = owners.find { it.correo == email }
                    if (user != null) {
                        print(user.id)
                        navController.navigate("petsList/${user.id}") // Pasa el ID del propietario como Int
                    } else {
                        // Manejar el caso en que el usuario no se encuentra
                    }
                } else {
                    // Manejar el caso en que el email sea null
                }
            }
        } catch (ex: Exception) {
           Log.d("",""+ex.message)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.logoperro), // Replace R.drawable.your_image with your image resource
                contentDescription = "Logo",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            if (showLoginForm.value) {
                Text(text = "Iniciar Sesión")
                UserForm(isCreateAccount = false) { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password) {
                        try {
                            val user = owners.find { it.correo == email && it.contrasena == Encyptado.devuelveEncryptado(password) }
                            if (user != null) {
                                print(user.id)
                                navController.navigate("petsList/${user.id}") // Pasa el ID del propietario como Int
                            } else {

                            }

                        }catch (ex:Exception){

                        }



                    }
                }
            } else {
                Text(text = "Crea una cuenta")
                UserForm(isCreateAccount = true) { email, password ->
                    Log.d("Veterinaria", "Funciona")
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate("createOwner/${email}/${password}")
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                val text1 = if (showLoginForm.value) "No tienes cuenta?" else "Inicia sesión"
                val text2 = if (showLoginForm.value) "No tienes cuenta?" else "Inicia sesión"
                Text(text = text1)
                Text(
                    text = text2,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        val opciones = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSignInClient = GoogleSignIn.getClient(context, opciones)

                        // Sign out to force account chooser
                        googleSignInClient.signOut().addOnCompleteListener {
                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logogoogle),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                )
                Text(
                    text = "Login Con Google",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        navController.navigate("mapa")
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.maps), // Cambia el id de la imagen según sea necesario
                    contentDescription = "",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                )
                Text(
                    text = "Maps",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        }
    }




@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(isCreateAccount: Boolean = false, onDone: (String, String) -> Unit = { _, _ -> }) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(emailState = email)
        PasswordInput(passwordState = password, labelId = "Password", passwordVisible = passwordVisible)
        SubmitButton(textId = if (isCreateAccount) "Crear Cuenta" else "Login", inputValido = valido) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValido
    ) {
        Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            }
        }
    )
}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image = if (passwordVisible.value) {
        Icons.Default.VisibilityOff
    } else {
        Icons.Default.Visibility
    }

    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(imageVector = image, contentDescription = "")
    }
}

@Composable
fun EmailInput(emailState: MutableState<String>, labelId: String = "email") {
    InputField(valueState = emailState, labelId = labelId, isSingleLine = true, keyboardType = KeyboardType.Email)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(valueState: MutableState<String>, labelId: String, isSingleLine: Boolean = true, keyboardType: KeyboardType) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}
