package com.example.ecoroute

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Cargar el mapa por defecto cuando la app inicia por primera vez
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, MapFragment())
                .commit()
        }

        // 2. Configurar la navegación para tus otros botones inferiores
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            // Aquí enlazamos las opciones del menú con tus distintos archivos
            var selectedFragment: Fragment = MapFragment()

            // Nota: Asegúrate de que los R.id coincidan con los que tienes en tu archivo res/menu/bottom_nav_menu.xml
            when (item.itemId) {
                // Si tienes un id llamado nav_mapa en tu menú
                // R.id.nav_mapa -> selectedFragment = MapFragment()

                // Cuando pasemos a la brújula peatonal
                // R.id.nav_brujula -> selectedFragment = CompassFragment()

                // Para tu bitácora de paradas
                // R.id.nav_log -> selectedFragment = LogFragment()
            }

            // Cambiamos el fragmento visible en pantalla
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, selectedFragment)
                .commit()

            true
        }
    }
}