package com.example.veterinariav2.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object Encyptado {

    fun devuelveEncryptado(contrasena: String): String {
        val hexString = StringBuilder()

        val salt = "hola"

        val saltedText = contrasena + salt

        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(saltedText.toByteArray(StandardCharsets.UTF_8))

            for (b in hash) {
                var hex = Integer.toHexString(0xff and b.toInt())

                if (hex.length == 1) {
                    hex = "0$hex"
                }
                hexString.append(hex)
            }
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        }

        return hexString.toString()
    }
}
