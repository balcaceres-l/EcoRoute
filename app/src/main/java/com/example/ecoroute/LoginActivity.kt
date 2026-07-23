package com.example.ecoroute

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ecoroute.data.remote.request.LoginRequest
import com.example.ecoroute.data.remote.response.ErrorResponse
import com.example.ecoroute.repository.AuthRepository
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(email, password)
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = authRepository.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val authData = response.body()
                    if (authData?.accessToken != null) {
                        Log.d("EcoRouteAuth", "Login exitoso para: $email")
                        Toast.makeText(this@LoginActivity, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
                        
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else if (authData?.user != null) {
                        // El usuario existe pero quizás falta confirmación de email
                        Toast.makeText(this@LoginActivity, "Por favor, verifica tu correo electrónico", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorDetail = parseError(errorBody)
                    Toast.makeText(this@LoginActivity, "Error: $errorDetail", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("EcoRouteAuth", "Error de red durante login", e)
                Toast.makeText(this@LoginActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun parseError(json: String?): String {
        if (json.isNullOrEmpty()) return "Error desconocido"
        return try {
            val errorResponse = Gson().fromJson(json, ErrorResponse::class.java)
            errorResponse.getDetail()
        } catch (e: Exception) {
            "Error al procesar respuesta: $json"
        }
    }
}
