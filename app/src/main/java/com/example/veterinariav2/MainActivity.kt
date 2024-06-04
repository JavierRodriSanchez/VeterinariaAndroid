package com.example.veterinariav2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.veterinariav2.data.PagosServiceByClient

import com.example.veterinariav2.maps.MyGoogleMaps

import com.example.veterinariav2.navigation.CitasTextField
import com.example.veterinariav2.navigation.ListaPagos


import com.example.veterinariav2.navigation.LoginVeterinariaScreen
import com.example.veterinariav2.navigation.MedicineListScreen
import com.example.veterinariav2.navigation.OwnerListScreen

import com.example.veterinariav2.navigation.TipoMedicinaListScreen
import com.example.veterinariav2.posts.CreateCitaScreen
import com.example.veterinariav2.posts.CreateOwnerScreen
import com.example.veterinariav2.posts.CreatePetScreen
import com.example.veterinariav2.ui.theme.VeterinariaV2Theme
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class MainActivity : ComponentActivity() {
    companion object {
        const val LOAD_PAYMENT_DATA_REQUEST_CODE = 991456
    }

    private lateinit var paymentsClient: PaymentsClient
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentsClient = Wallet.getPaymentsClient(
            this,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build()
        )





        setContent {
            VeterinariaV2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            //PaymentScreen { requestPayment() }
                       LoginVeterinariaScreen(navController = navController)
                        }

                        composable(
                            "listaPagos/{ownerId}",
                            arguments = listOf(navArgument("ownerId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val ownerId = backStackEntry.arguments?.getInt("ownerId")
                            ownerId?.let {
                                ListaPagos(navController= navController, ownerId = it)
                            }
                        }

                        composable(
                            "petsList/{ownerId}",
                            arguments = listOf(navArgument("ownerId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val ownerId = backStackEntry.arguments?.getInt("ownerId")
                            ownerId?.let {
                                OwnerListScreen(navController = navController, ownerId = it)
                            }
                        }
                        composable(
                            "createOwner/{email}/{password}",
                            arguments = listOf(
                                navArgument("email") { type = NavType.StringType },
                                navArgument("password") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            val password = backStackEntry.arguments?.getString("password") ?: ""
                            CreateOwnerScreen(navController = navController, email = email, password = password)
                        }
                        composable(
                            "createPet/{ownerId}",
                            arguments = listOf(navArgument("ownerId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val ownerId = backStackEntry.arguments?.getInt("ownerId")

                            CreatePetScreen(navController = navController, ownerId = ownerId)
                        }

                        composable(
                            "createCita/{petId}/{ownerId}",
                            arguments = listOf(
                                navArgument("petId") { type = NavType.IntType },
                                navArgument("ownerId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val petId = backStackEntry.arguments?.getInt("petId")
                            val ownerId = backStackEntry.arguments?.getInt("ownerId")
                            CreateCitaScreen(
                                navController = navController,
                                petId = petId,
                                ownerId = ownerId
                            )
                        }

                        //Posible borrado
                        composable(
                            "CitasList/{petId}",
                            arguments = listOf(navArgument("petId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val petId = backStackEntry.arguments?.getInt("petId")
                            petId?.let {
                                CitasTextField(navController = navController,petId)
                            }
                        }

                        composable("ListTipoMedicina/{ownerId}",
                            arguments = listOf(
                                navArgument("ownerId") { type = NavType.IntType })
                        ) { backStackEntry ->

                            val ownerId = backStackEntry.arguments?.getInt("ownerId")
                            ownerId?.let{
                                TipoMedicinaListScreen(navController = navController, ownerId = it)

                            }

                        }

                        composable("ListMedicines/{tipoId}/{ownerId}",
                            arguments = listOf(
                                navArgument("tipoId") { type = NavType.IntType },
                                navArgument("ownerId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val tipoId = backStackEntry.arguments?.getInt("tipoId") ?: 0
                            val ownerId = backStackEntry.arguments?.getInt("ownerId") ?: 0
                            MedicineListScreen(navController = navController, tipoId = tipoId, ownerId = ownerId){requestPayment()}
                        }


                        composable("mapa") {

                              MyGoogleMaps()

                        }
                    }
                }
            }}
    }


    fun requestPayment() {
        val baseRequest = JsonObject().apply {
            addProperty("apiVersion", 2)
            addProperty("apiVersionMinor", 0)
        }

        val allowedCardNetworks = JsonArray().apply {
            add("VISA")
            add("MASTERCARD")
        }

        val allowedCardAuthMethods = JsonArray().apply {
            add("PAN_ONLY")
            add("CRYPTOGRAM_3DS")
        }



        val cardPaymentMethod = JsonObject().apply {
            addProperty("type", "CARD")
            add("parameters", JsonObject().apply {
                add("allowedAuthMethods", JsonArray().apply {
                    add("PAN_ONLY")
                    add("CRYPTOGRAM_3DS")
                })
                add("allowedCardNetworks", JsonArray().apply {
                    add("AMEX")
                    add("DISCOVER")
                    add("JCB")
                    add("MASTERCARD")
                    add("VISA")
                })
            })
            add("tokenizationSpecification", JsonObject().apply {
                addProperty("type", "PAYMENT_GATEWAY")
                add("parameters", JsonObject().apply {
                    addProperty("gateway", "example")
                    addProperty("gatewayMerchantId", "exampleGatewayMerchantId")
                })
            })
        }


        val paymentDataRequest = baseRequest.deepCopy().apply {
            add("allowedPaymentMethods", JsonArray().apply { add(cardPaymentMethod) })
            add("transactionInfo", JsonObject().apply {
                addProperty("totalPriceStatus", "FINAL")
                addProperty("totalPrice", "10.00") // Precio total de la transacción
                addProperty("currencyCode", "USD")
            })
            add("merchantInfo", JsonObject().apply {
                addProperty("merchantName", "Example Merchant")
                addProperty("merchantId", "12345678901234567890") // ID del comerciante proporcionado por Google
            })
        }


        val request = PaymentDataRequest.fromJson(paymentDataRequest.toString())
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(request),
            this,
            LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val paymentData = PaymentData.getFromIntent(data!!)
                        // Manejar el pago exitoso aquí
                    }
                    RESULT_CANCELED -> {
                        // El usuario canceló el flujo de pago
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        // Manejar el error
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(onPayClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onPayClick) {
            Text(text = "Pagar con Google Pay")
        }
    }
}











