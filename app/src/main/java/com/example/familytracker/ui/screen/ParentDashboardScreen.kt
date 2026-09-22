package com.example.familytracker.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.familytracker.data.model.LocationPoint
import com.example.familytracker.data.repository.LocationRepository

/**
 * NB: remplacer le Text() de coordonnées par un vrai SDK carte (Google Maps Compose,
 * dépendance com.google.maps.android:maps-compose) une fois la clé API configurée.
 */
@Composable
fun ParentDashboardScreen(
    familyId: String,
    childUid: String,
    locationRepository: LocationRepository = LocationRepository()
) {
    var point by remember { mutableStateOf<LocationPoint?>(null) }

    LaunchedEffect(childUid) {
        locationRepository.observeChildLocation(familyId, childUid)
            .collect { point = it }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Position de l'enfant", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        point?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Lat: ${it.lat}")
                    Text("Lng: ${it.lng}")
                    Text("Dernière mise à jour: ${it.timestamp}")
                }
            }
        } ?: Text("En attente de la première position...")
    }
}
