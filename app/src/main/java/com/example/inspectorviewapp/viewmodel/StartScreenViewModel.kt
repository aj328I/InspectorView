package com.example.inspectorviewapp.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the StartScreen. Manages camera, heading, GPS, and timestamp state.
 * Uses Hilt for dependency injection.
 */
@HiltViewModel
class StartScreenViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application), SensorEventListener {

    private val context: Context = application.applicationContext

    // StateFlows for UI
    private val _heading = MutableStateFlow(319) // Dummy default
    val heading: StateFlow<Int> = _heading.asStateFlow()

    private val _latitude = MutableStateFlow("37.42") // Dummy default
    val latitude: StateFlow<String> = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow("-122.08") // Dummy default
    val longitude: StateFlow<String> = _longitude.asStateFlow()

    private val _timestamp = MutableStateFlow(getCurrentTimestamp())
    val timestamp: StateFlow<String> = _timestamp.asStateFlow()

    private val _cameraPermissionDenied = MutableStateFlow(false)
    val cameraPermissionDenied: StateFlow<Boolean> = _cameraPermissionDenied.asStateFlow()

    private val _locationPermissionDenied = MutableStateFlow(false)
    val locationPermissionDenied: StateFlow<Boolean> = _locationPermissionDenied.asStateFlow()

    // Sensor and location
    private var sensorManager: SensorManager? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        startHeadingUpdates()
        startLocationUpdates()
        startTimestampUpdates()
    }

    /**
     * Start listening for heading (azimuth) updates using SensorManager.
     */
    private fun startHeadingUpdates() {
        try {
            val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            if (sensor != null) {
                sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
            } else {
                Timber.w("Orientation sensor not available, using dummy heading.")
            }
        } catch (e: Exception) {
            Timber.e(e, "Error starting heading updates")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                val azimuth = it.values[0].toInt()
                _heading.value = azimuth
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    /**
     * Start location updates using FusedLocationProviderClient.
     */
    private fun startLocationUpdates() {
        try {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .build()
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc: Location? = result.lastLocation
                    if (loc != null) {
                        _latitude.value = String.format(Locale.US, "%.2f", loc.latitude)
                        _longitude.value = String.format(Locale.US, "%.2f", loc.longitude)
                    }
                }
            }
            fusedLocationClient?.requestLocationUpdates(request, locationCallback!!, null)
        } catch (e: SecurityException) {
            Timber.w("Location permission denied, using dummy location.")
            _locationPermissionDenied.value = true
        } catch (e: Exception) {
            Timber.e(e, "Error starting location updates")
        }
    }

    /**
     * Start updating the timestamp every second.
     */
    private fun startTimestampUpdates() {
        viewModelScope.launch {
            while (true) {
                _timestamp.value = getCurrentTimestamp()
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
    }

    fun onCameraPermissionDenied() {
        Timber.w("Camera permission denied by user.")
        _cameraPermissionDenied.value = true
    }

    fun onCameraPermissionGranted() {
        Timber.d("Camera permission granted by user.")
        _cameraPermissionDenied.value = false
    }

    fun onLocationPermissionDenied() {
        Timber.w("Location permission denied by user.")
        _locationPermissionDenied.value = true
    }

    fun onLocationPermissionGranted() {
        Timber.d("Location permission granted by user.")
        _locationPermissionDenied.value = false
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager?.unregisterListener(this)
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
} 