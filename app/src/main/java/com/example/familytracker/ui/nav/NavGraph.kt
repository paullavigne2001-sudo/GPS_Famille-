package com.example.familytracker.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.familytracker.data.model.Role
import com.example.familytracker.ui.screen.ChildTrackingScreen
import com.example.familytracker.ui.screen.LoginScreen
import com.example.familytracker.ui.screen.ParentDashboardScreen

private const val ROUTE_LOGIN = "login"
private const val ROUTE_PARENT = "parent_dashboard"
private const val ROUTE_CHILD = "child_tracking"

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = ROUTE_LOGIN) {

        composable(ROUTE_LOGIN) {
            LoginScreen { profile ->
                val destination = if (profile.role == Role.PARENT) ROUTE_PARENT else ROUTE_CHILD
                navController.navigate(destination) {
                    popUpTo(ROUTE_LOGIN) { inclusive = true }
                }
            }
        }

        composable(ROUTE_PARENT) {
            // TODO: remplacer "child-uid-placeholder" par la sélection réelle de l'enfant
            // (liste des membres de la famille avec role == CHILD, requête sur users/).
            ParentDashboardScreen(familyId = "family-id-placeholder", childUid = "child-uid-placeholder")
        }

        composable(ROUTE_CHILD) {
            ChildTrackingScreen()
        }
    }
}
