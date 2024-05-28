package com.example.veterinariav2.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel:ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)

    fun createUserWithEmailAndPassword(email:String,password:String,home:()->Unit){
        if(_loading.value == false)
        {
            _loading.value == true
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                task->
                if(task.isSuccessful){
                    home()
                }else{
                    Log.d("Veterinaria","Fallo de creación usuario")
                }
                _loading.value == false
            }
        }
    }
    fun signInWithGoogleCredential(credential: AuthCredential,home: () -> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithCredential( credential).addOnCompleteListener{
                task->
                if(task.isSuccessful){
                    Log.d("Veterinaria","Logeado con Google exitoso")
                    home()
                }


            }
                .addOnFailureListener{
                    Log.d("Veterinaria","Logeado con Google fallido")
                }
        }catch (ex:Exception){
            Log.d("Veterinaria","${ex.message}")
        }
    }

    fun signInWithEmailAndPassword(email:String,password:String,home:()->Unit)
            = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d("Veterinaria","signInWithEmailAndPassword logeado")
                    home()
                } else {
                    Log.d("Veterinaria","signInWithEmailAndPassword ${task.result.toString()}")
                }
            }.addOnFailureListener { ex ->
                Log.d("Veterinaria", "signInWithEmailAndPassword ${ex.message}")
                // Aquí puedes realizar acciones adicionales si lo deseas,
                // como mostrar un mensaje de error al usuario.
            }
        } catch (ex: Exception) {
            Log.d("Veterinaria", "signInWithEmailAndPassword ${ex.message}")
            // También puedes manejar excepciones generales aquí si es necesario.
        }
    }}


