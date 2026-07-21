package com.example.ecoroute

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Al pasarle el R.layout en el constructor, nos ahorramos código extra
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // Este método se ejecuta justo cuando la vista (el XML) ya se dibujó
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Buscamos el mapa en el XML y le decimos que nos avise cuando esté listo
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.google_map_container) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
    }

    // Este método se dispara automáticamente cuando Google Maps cargó con éxito
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Coordenadas locales de Santa Ana para probar tu proyecto
        val santaAna = LatLng(13.9778, -89.5639)

        // Colocamos el marcador simulando una bolsa reportada
        mMap.addMarker(
            MarkerOptions()
                .position(santaAna)
                .title("Recolección: 3 bolsas PET")
        )

        // Hacemos un "zoom" hacia el marcador (15f es nivel de calle)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaAna, 15f))
    }
}