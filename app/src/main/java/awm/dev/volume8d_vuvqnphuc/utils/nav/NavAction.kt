package awm.dev.volume8d_vuvqnphuc.utils.nav

import androidx.navigation.NavHostController

class NavAction(private val navController: NavHostController) {
    private fun <T : Any> navSingleToTop(route: T) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
    fun navToLanguageScreen(showFrom: String) {
        navSingleToTop(route = LanguageRoute(showFrom))
    }
    fun navToMainScreen() {
        navSingleToTop(route = MainRoute)
    }
    fun navToInstructionsScreen() {
        navSingleToTop(route = InstructionsRoute)
    }
}