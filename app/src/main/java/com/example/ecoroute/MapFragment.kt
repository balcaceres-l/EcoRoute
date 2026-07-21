package com.example.ecoroute

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // Registrar la solicitud de permisos en pantalla
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            habilitarUbicacion()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.google_map_container) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Marcador de prueba existente en Santa Ana
        val santaAna = LatLng(13.9778, -89.5639)
        mMap.addMarker(MarkerOptions().position(santaAna).title("Recolección: 3 bolsas PET"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaAna, 15f))

        // Botones de Zoom anteriores
        view?.findViewById<ImageButton>(R.id.btnZoomIn)?.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
        view?.findViewById<MaterialButton>(R.id.btnZoomOut)?.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        // Configurar el botón de Mi Ubicación (La Cruz)
        view?.findViewById<FloatingActionButton>(R.id.btnMyLocation)?.setOnClickListener {
            verificarPermisosYCentrar()
        }
    }

    private fun verificarPermisosYCentrar() {
        context?.let { ctx ->
            when {
                // Si el permiso ya está otorgado
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED -> {
                    habilitarUbicacion()
                    mMap.myLocation?.let { loc ->
                        val currentLatLng = LatLng(loc.latitude, loc.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    }
                }
                // Si no está otorgado, se le pide al usuario en pantalla
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    private fun habilitarUbicacion() {
        try {
            // Habilita la capa nativa de Google Maps (el punto azul y el botón nativo de la esquina)
            mMap.isMyLocationEnabled = true

            // Ocultamos el botón nativo de Google de la esquina superior porque usaremos el tuyo personalizado abajo
            mMap.uiSettings.isMyLocationButtonEnabled = false
        } catch (e: SecurityException) {
            // Manejo de excepción seguro si los permisos fallan
        }
    }
}