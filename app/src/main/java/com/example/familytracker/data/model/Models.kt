package com.example.familytracker.data.model

enum class Role { PARENT, CHILD }

/** Document Firestore : users/{uid} */
data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val role: Role = Role.CHILD,
    val familyId: String = ""
)

/** Document Firestore : families/{familyId} */
data class Family(
    val id: String = "",
    val name: String = ""
)

/** Sous-collection : families/{familyId}/locations/{childUid} — dernière position connue */
data class LocationPoint(
    val userId: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val timestamp: Long = 0L,
    val batteryLevel: Int = -1
)

/** Sous-collection : families/{familyId}/zones/{zoneId} */
data class Zone(
    val id: String = "",
    val name: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val radiusMeters: Float = 100f,
    val createdBy: String = ""
)
