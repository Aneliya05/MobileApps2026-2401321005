package bg.uni.plovdiv.fmi.medassist.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Guide : Screen("guide")
    data object Map : Screen("map")
    data object Contacts : Screen("contacts")
    data object Kit : Screen("kit")
    data object Settings : Screen("settings")
}