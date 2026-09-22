# Family Tracker — squelette Android (Kotlin + Compose + Firebase)

Une seule app, deux rôles (`parent` / `child`) déterminés après login via Firestore.

## Structure

```
app/src/main/java/com/example/familytracker/
├── data/
│   ├── model/          -> User, Family, LocationPoint, Zone
│   └── repository/     -> AuthRepository, LocationRepository
├── service/             -> LocationTrackingService (Foreground Service)
├── ui/
│   ├── nav/             -> NavGraph (routage selon le rôle)
│   └── screen/          -> LoginScreen, ParentDashboardScreen, ChildTrackingScreen
firestore.rules          -> règles de sécurité côté serveur
```

## Mise en route

1. Créer un projet Firebase, activer **Auth (email/mot de passe)** et **Firestore**.
2. Télécharger `google-services.json` et le placer dans `app/`.
3. Ajouter le plugin `com.google.gms.google-services` au `build.gradle` racine (non fourni ici, standard Firebase).
4. Déployer `firestore.rules` : `firebase deploy --only firestore:rules`.
5. Créer manuellement un premier document `families/{familyId}` et deux `users/{uid}` (role `parent` et `child`) pour tester, ou coder un écran d'inscription (non inclus dans ce squelette).

## Ce qui manque volontairement (à toi de compléter)

- Écran d'inscription / création de famille (invite code).
- Historique de trajet (liste des `LocationPoint` sur une carte, actuellement seul le point courant est géré).
- Gestion des `Zone` (création/édition des geofences) — le modèle existe, pas l'UI.
- Bouton SOS.
- Icône, thème, tests.

## Permissions Play Store

`ACCESS_BACKGROUND_LOCATION` nécessite un formulaire de justification dans la Play Console (section "App content > Permissions sensibles"). Prévoir des captures d'écran de l'écran d'explication avant soumission.
