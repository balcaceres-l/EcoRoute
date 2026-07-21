package com.example.ecoroute

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar el mapa por defecto
        if (savedInstanceState == null) {
            cargarFragmento(MapFragment())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Reemplaza estos R.id con los que tengas en tu bottom_nav_menu.xml
                R.id.nav_map -> {
                    cargarFragmento(MapFragment())
                    true
                }
                R.id.nav_compass -> {
                    cargarFragmento(CompassFragment())
                    true
                }
                R.id.nav_log -> {
                    cargarFragmento(LogFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Función auxiliar para no repetir código al cambiar de vista
    private fun cargarFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}