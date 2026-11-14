package bg.uni.plovdiv.fmi.medassist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigationevent.OnBackInvokedInput
import bg.uni.plovdiv.fmi.medassist.presentation.contacts.ContactsScreen
import bg.uni.plovdiv.fmi.medassist.presentation.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Помощна функция за навигация без натрупване на излишни екрани в стека
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
                onNavigateToKit = { navigateSingleTop(Screen.Kit.route) },
                onNavigateToSettings = { navigateSingleTop(Screen.Settings.route) }
            )
        }

        composable(Screen.Guide.route) {
            // Тук ще извикаме GuideScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Map.route) {
            // Тук ще извикаме MapScreen()
        }

        composable(Screen.Contacts.route) {
            ContactsScreen(
                onBack = {navController.popBackStack() }
            )
        }

        composable(Screen.Kit.route) {
            // Тук ще извикаме KitScreen()
        }

        composable(Screen.Settings.route) {
            // Тук ще извикаме SettingsScreen()
        }
    }
}