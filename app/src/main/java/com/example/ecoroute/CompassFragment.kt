package com.example.ecoroute

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment

class CompassFragment : Fragment(R.layout.fragment_compass), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    // Usaremos arreglos fijos y booleanos para el nuevo Filtro de Suavizado
    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private var hasGravity = false
    private var hasGeomagnetic = false

    private var currentDegree = 0f
    private var needleImage: ImageView? = null

    // Constante para el Filtro de Paso Bajo (entre más cerca a 0, más suave pero más lenta la respuesta)
    private val ALPHA = 0.1f

    // Ubicaciones simuladas
    private val myLocation = Location("gps").apply {
        latitude = 13.9750
        longitude = -89.5600
    }

    private val stopLocation = Location("gps").apply {
        latitude = 13.9778
        longitude = -89.5639
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        needleImage = view.findViewById(R.id.compassNeedle)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        // Cambiamos a SENSOR_DELAY_GAME para una tasa de refresco más adecuada para animaciones
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // Función matemática para filtrar y suavizar el temblor de la mano
    private fun lowPass(input: FloatArray, output: FloatArray) {
        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            lowPass(event.values, gravity)
            hasGravity = true
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            lowPass(event.values, geomagnetic)
            hasGeomagnetic = true
        }

        if (hasGravity && hasGeomagnetic) {
            val R = FloatArray(9)
            val I = FloatArray(9)

            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)

                val azimuthInRadians = orientation[0]
                // Convertimos el azimuth a grados (-180 a 180)
                val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

                // Calculamos el rumbo hacia la parada de reciclaje
                val bearing = myLocation.bearingTo(stopLocation)

                // Este es el ángulo teórico ideal al que debería apuntar la aguja
                val targetRotation = bearing - azimuthInDegrees

                // --- INTERPOLACIÓN ANGULAR (ELIMINA VIBRACIÓN Y GIROS LOCOS) ---
                // Calculamos la diferencia entre el ángulo actual de la aguja y el objetivo
                var diff = targetRotation - currentDegree


                while (diff < -180) diff += 360
                while (diff > 180) diff -= 360

                // Factor de amortiguación: 0.10f significa que avanza un 10% hacia el objetivo en cada ciclo.
                // Esto elimina el temblor de la mano por completo sin congelar la aguja.
                currentDegree += diff * 0.10f

                // Asignar el valor suavizado a la propiedad de rotación nativa
                needleImage?.rotation = currentDegree
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // En un escenario ideal aquí podríamos avisar si hace falta calibrar haciendo un '8' con el celular
    }
}