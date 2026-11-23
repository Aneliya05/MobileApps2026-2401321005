package bg.uni.plovdiv.fmi.medassist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import bg.uni.plovdiv.fmi.medassist.presentation.contacts.ContactsScreen
import bg.uni.plovdiv.fmi.medassist.presentation.guide.GuideScreen
import bg.uni.plovdiv.fmi.medassist.presentation.home.HomeScreen
import bg.uni.plovdiv.fmi.medassist.presentation.kit.HealthProfileScreen
import bg.uni.plovdiv.fmi.medassist.presentation.map.MapScreen
import bg.uni.plovdiv.fmi.medassist.presentation.settings.SettingsScreen

@Composable
fun AppNavigation(
    isDarkMode: Boolean = false,
    onDarkModeChanged: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    fun navigateSingleTop(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGuide = { navigateSingleTop(Screen.Guide.route) },
                onNavigateToMap = { navigateSingleTop(Screen.Map.route) },
                onNavigateToContacts = { navigateSingleTop(Screen.Contacts.route) },
                onNavigateToHealthProfile = { navigateSingleTop(Screen.HealthProfile.route) },
                onNavigateToSettings = { navigateSingleTop(Screen.Settings.route) }
            )
        }

        composable(Screen.Guide.route) {
            GuideScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Map.route) {
            MapScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Contacts.route) {
            ContactsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.HealthProfile.route) {
            HealthProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                isDarkMode = isDarkMode,
                onDarkModeChanged = onDarkModeChanged
            )
        }
    }
}
