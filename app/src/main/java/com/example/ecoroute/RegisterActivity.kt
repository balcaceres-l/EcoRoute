package com.example.ecoroute

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ecoroute.data.remote.request.ProfileRequest
import com.example.ecoroute.data.remote.request.RegisterRequest
import com.example.ecoroute.data.remote.response.ErrorResponse
import com.example.ecoroute.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)
        val actvRole = findViewById<AutoCompleteTextView>(R.id.actvRole)

        // Configurar el dropdown de roles
        val roles = arrayOf("Residente", "Recolector")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        actvRole.setAdapter(adapter)
        // Valor por defecto
        actvRole.setText(roles[0], false)

        tvLogin.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val selectedRole = actvRole.text.toString()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbTerms.isChecked) {
                Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mapear el rol seleccionado al valor que espera la base de datos
            val roleDbValue = if (selectedRole == "Residente") "resident" else "collector"

            performRegister(email, password, fullName, roleDbValue)
        }
    }

    private fun performRegister(email: String, password: String, fullName: String, role: String) {
        lifecycleScope.launch {
            try {
                // 1. Registrar usuario en Supabase Auth
                val registerResponse = authRepository.register(RegisterRequest(email, password))
                
                if (registerResponse.isSuccessful) {
                    val authData = registerResponse.body()
                    if (authData?.user != null && authData.accessToken != null) {
                        // 2. Crear perfil en la tabla "profiles"
                        val profileRequest = ProfileRequest(
                            id = authData.user.id,
                            full_name = fullName,
                            role = role
                        )
                        val profileResponse = authRepository.createProfile(authData.accessToken, profileRequest)
                        
                        if (profileResponse.isSuccessful) {
                            Log.d("EcoRouteAuth", "Base de datos conectada exitosamente: Usuario y Perfil ($role) creados")
                            Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            val errorBody = profileResponse.errorBody()?.string()
                            val errorDetail = parseError(errorBody)
                            Toast.makeText(this@RegisterActivity, "Error al crear perfil: $errorDetail", Toast.LENGTH_LONG).show()
                        }
                    } else if (authData?.user != null) {
                        // Caso donde se requiere confirmación de email y no hay token aún
                        Toast.makeText(this@RegisterActivity, "Registro exitoso. Por favor verifica tu correo.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                } else {
                    val errorBody = registerResponse.errorBody()?.string()
                    val errorDetail = parseError(errorBody)
                    Toast.makeText(this@RegisterActivity, "Error en registro: $errorDetail", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
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
