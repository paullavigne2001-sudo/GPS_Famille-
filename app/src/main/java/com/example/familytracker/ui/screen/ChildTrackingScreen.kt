package com.example.familytracker.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.familytracker.service.LocationTrackingService

@Composable
fun ChildTrackingScreen() {
    val context = LocalContext.current
    var tracking by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Statut du suivi", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Text(if (tracking) "Partage de position actif ✅" else "Partage de position désactivé")
        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            tracking = !tracking
            if (tracking) LocationTrackingService.start(context)
            else LocationTrackingService.stop(context)
        }) {
            Text(if (tracking) "Désactiver" else "Activer le suivi")
        }

        // NOTE: les permissions ACCESS_FINE_LOCATION puis ACCESS_BACKGROUND_LOCATION
        // doivent être demandées AVANT d'appeler start() (rationale UI non inclus ici,
        // utiliser androidx.activity.result.contract.ActivityResultContracts.RequestPermission).
    }
}
