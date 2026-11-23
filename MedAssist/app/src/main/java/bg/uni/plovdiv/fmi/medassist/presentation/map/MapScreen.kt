package bg.uni.plovdiv.fmi.medassist.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import bg.uni.plovdiv.fmi.medassist.data.remote.NominatimApiService
import bg.uni.plovdiv.fmi.medassist.ui.theme.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

data class HospitalItem(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Float
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val apiService = remember { NominatimApiService.create() }

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    var userLat by remember { mutableStateOf<Double?>(null) }
    var userLng by remember { mutableStateOf<Double?>(null) }
    var focusedPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var hospitals by remember { mutableStateOf<List<HospitalItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 1. Взимаме последната известна, за да не чакаме на празен екран
        val lastLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (lastLoc != null) {
            userLat = lastLoc.latitude
            userLng = lastLoc.longitude
            focusedPoint = GeoPoint(lastLoc.latitude, lastLoc.longitude)
        }

        // 2. Заявяваме СВЕЖА локация (за да хване веднага промяната от емулатора!)
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                userLat = location.latitude
                userLng = location.longitude
                focusedPoint = GeoPoint(location.latitude, location.longitude)
                lm.removeUpdates(this)
            }
        }
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, listener)
        }
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, listener)
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
        if (granted) startLocationTracking()
        else {
            isLoading = false
            statusMessage = "Location permission is required to find nearby hospitals."
        }
    }

    LaunchedEffect(Unit) {
        if (hasLocationPermission) startLocationTracking()
        else permissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    // Когато имаме реални GPS координати, търсим болниците около тях чрез Nominatim API
    LaunchedEffect(userLat, userLng) {
        val lat = userLat ?: return@LaunchedEffect
        val lng = userLng ?: return@LaunchedEffect
        isLoading = true
        statusMessage = null
        try {
            val delta = 0.08 // ~8-9 км радиус около текущата локация
            val viewbox = "${lng - delta},${lat + delta},${lng + delta},${lat - delta}"
            val results = apiService.searchHospitals(viewbox = viewbox)

            hospitals = results.mapNotNull { place ->
                val pLat = place.lat.toDoubleOrNull() ?: return@mapNotNull null
                val pLng = place.lon.toDoubleOrNull() ?: return@mapNotNull null

                val dist = FloatArray(1)
                Location.distanceBetween(lat, lng, pLat, pLng, dist)

                val cleanName = place.name?.takeIf { it.isNotBlank() }
                    ?: place.displayName.substringBefore(",")

                HospitalItem(
                    id = place.placeId,
                    name = cleanName,
                    address = place.displayName,
                    latitude = pLat,
                    longitude = pLng,
                    distanceKm = dist[0] / 1000f
                )
            }.sortedBy { it.distanceKm }

            if (hospitals.isEmpty()) {
                statusMessage = "No hospitals found in this area."
            }
        } catch (e: Exception) {
            statusMessage = "Network error while searching hospitals: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = ScreenBackground,
        topBar = {
            TopAppBar(
                title = { Text("Nearby Hospitals", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (hasLocationPermission) {
                                startLocationTracking()
                                if (userLat != null && userLng != null) {
                                    focusedPoint = GeoPoint(userLat!!, userLng!!)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location",
                            tint = EmergencyRedLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 1. КАРТА С РЕАЛНОТО МЕСТОПОЛОЖЕНИЕ И БОЛНИЦИТЕ
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.42f),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CardBorderGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                if (focusedPoint != null && userLat != null && userLng != null) {
                    AndroidView(
                        factory = { ctx ->
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(14.0)
                            }
                        },
                        update = { mapView ->
                            mapView.controller.animateTo(focusedPoint)
                            mapView.overlays.clear()

                            val userMarker = Marker(mapView).apply {
                                position = GeoPoint(userLat!!, userLng!!)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = "My Location"
                            }
                            mapView.overlays.add(userMarker)

                            hospitals.forEach { hospital ->
                                val marker = Marker(mapView).apply {
                                    position = GeoPoint(hospital.latitude, hospital.longitude)
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = hospital.name
                                    snippet = String.format(Locale.US, "%.1f km", hospital.distanceKm)
                                }
                                mapView.overlays.add(marker)
                            }
                            mapView.invalidate()
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(18.dp))
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = EmergencyRedLight)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Waiting for GPS signal...", fontSize = 13.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Hospitals Near You (${hospitals.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 2. СПИСЪК С БОЛНИЦИТЕ + БУТОН ЗА УПЪТВАНИЯ В GOOGLE MAPS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.58f)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            color = EmergencyRedLight,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    statusMessage != null && hospitals.isEmpty() -> {
                        Text(
                            text = statusMessage!!,
                            color = TextSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(hospitals, key = { it.id }) { hospital ->
                                val isSelected = focusedPoint?.latitude == hospital.latitude &&
                                        focusedPoint?.longitude == hospital.longitude

                                Card(
                                    onClick = { focusedPoint = GeoPoint(hospital.latitude, hospital.longitude) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) SoftPinkBanner else Color.White
                                    ),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) EmergencyRedLight else CardBorderGray
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .background(if (isSelected) Color.White else SoftPinkBanner, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.LocalHospital,
                                                    contentDescription = null,
                                                    tint = EmergencyRedLight
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = hospital.name,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp,
                                                    color = TextPrimary
                                                )
                                                Text(
                                                    text = hospital.address,
                                                    fontSize = 12.sp,
                                                    color = TextSecondary,
                                                    maxLines = 2
                                                )
                                            }
                                            Text(
                                                text = String.format(Locale.US, "%.1f km", hospital.distanceKm),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = EmergencyRedDark
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Button(
                                            onClick = {
                                                val navUri = Uri.parse("google.navigation:q=${hospital.latitude},${hospital.longitude}")
                                                val navIntent = Intent(Intent.ACTION_VIEW, navUri).apply {
                                                    setPackage("com.google.android.apps.maps")
                                                }
                                                try {
                                                    context.startActivity(navIntent)
                                                } catch (e: Exception) {
                                                    val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${hospital.latitude},${hospital.longitude}")
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRedLight),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Directions in Google Maps", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }
    }
}