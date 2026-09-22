package com.example.familytracker.data.repository

import com.example.familytracker.data.model.Role
import com.example.familytracker.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Auth + lecture du profil. Le rôle vient TOUJOURS de Firestore (jamais du client
 * seul) : les règles de sécurité (firestore.rules) revérifient ce même champ côté serveur.
 */
class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun login(email: String, password: String): Result<UserProfile> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: error("Auth sans UID")
        val profile = fetchProfile(uid)
        Result.success(profile)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun fetchProfile(uid: String): UserProfile {
        val doc = db.collection("users").document(uid).get().await()
        val roleStr = doc.getString("role") ?: "child"
        return UserProfile(
            uid = uid,
            displayName = doc.getString("displayName") ?: "",
            role = if (roleStr == "parent") Role.PARENT else Role.CHILD,
            familyId = doc.getString("familyId") ?: ""
        )
    }

    fun currentUid(): String? = auth.currentUser?.uid

    fun logout() = auth.signOut()
}
