package com.example.veterinariav2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.veterinariav2.maps.MyGoogleMaps

import com.example.veterinariav2.navigation.CitasTextField


import com.example.veterinariav2.navigation.LoginVeterinariaScreen
import com.example.veterinariav2.navigation.MedicineListScreen
import com.example.veterinariav2.navigation.OwnerListScreen
import com.example.veterinariav2.navigation.TipoMedicinaListScreen
import com.example.veterinariav2.posts.CreateCitaScreen
import com.example.veterinariav2.posts.CreateOwnerScreen
import com.example.veterinariav2.posts.CreatePetScreen
import com.example.veterinariav2.ui.theme.VeterinariaV2Theme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)






        setContent {
            VeterinariaV2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {

                       LoginVeterinariaScreen(navController = navController)
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
                            MedicineListScreen(navController = navController, tipoId = tipoId, ownerId = ownerId)
                        }


                        composable("mapa") {

                              MyGoogleMaps()

                        }
                    }
                }
            }}
    }}


