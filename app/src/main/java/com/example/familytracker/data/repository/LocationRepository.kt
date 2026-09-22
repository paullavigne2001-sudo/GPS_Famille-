package com.example.familytracker.data.repository

import com.example.familytracker.data.model.LocationPoint
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /** Appelé côté enfant par le Foreground Service à chaque nouvelle position. */
    suspend fun pushLocation(familyId: String, point: LocationPoint) {
        db.collection("families").document(familyId)
            .collection("locations").document(point.userId)
            .set(point)
            .let { it.result } // no-op si appelé hors coroutine Firestore Task; sinon utiliser .await()
    }

    /** Écoute temps réel côté parent pour un enfant donné. */
    fun observeChildLocation(familyId: String, childUid: String): Flow<LocationPoint?> = callbackFlow {
        val registration = db.collection("families").document(familyId)
            .collection("locations").document(childUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(null)
                    return@addSnapshotListener
                }
                trySend(snapshot?.toObject(LocationPoint::class.java))
            }
        awaitClose { registration.remove() }
    }
}
