package de.leberkasrechner.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.leberkasrechner.app.ui.theme.LeberkasrechnerTheme
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView

class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MapLibre with API key and tile server
        MapLibre.getInstance(this)

        // Request location permissions
        requestLocationPermissions()

        // Initialize MapView
        mapView = MapView(this)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapLibreMap ->
            mapLibreMap.setStyle("https://tiles.leberkasrechner.de/styles/osm-bright/style.json") {
                enableLocationComponent(mapLibreMap)
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(48.137154, 11.576124))
                    .zoom(8.0)
                    .build()
                mapLibreMap.cameraPosition = cameraPosition
            }
        }

        setContent {
            LeberkasrechnerTheme {
                HeaderBar()
                Surface( modifier = Modifier.padding(top = 40.dp)){
                Surface(color = Color.Yellow, modifier = Modifier.fillMaxSize()) {
                    MapViewContainer(mapView)
                }

                }
            }
        }
    }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun enableLocationComponent(mapLibreMap: MapLibreMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            val locationComponentOptions = LocationComponentOptions.builder(this)
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, mapLibreMap.style!!)
                .locationComponentOptions(locationComponentOptions)
                .build()

            val locationComponent = mapLibreMap.locationComponent
            locationComponent.activateLocationComponent(locationComponentActivationOptions)
            locationComponent.isLocationComponentEnabled = true
            locationComponent.cameraMode = org.maplibre.android.location.modes.CameraMode.TRACKING
            locationComponent.renderMode = org.maplibre.android.location.modes.RenderMode.COMPASS
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

@Composable
fun MapViewContainer(mapView: MapView) {
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}
@Composable
fun HeaderBar(){
   Text(
       text = "Leberkasrechner",
       fontSize =  27.sp,
       modifier = Modifier
           .padding(start = 6.dp)
           .padding(top = 4.dp)
   )
}
