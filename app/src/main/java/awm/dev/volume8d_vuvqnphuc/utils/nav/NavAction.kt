package awm.dev.volume8d_vuvqnphuc.utils.nav

import androidx.navigation.NavHostController

class NavAction(private val navController: NavHostController) {
    private fun <T : Any> navSingleToTop(route: T) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
    // dùng ở splass
    fun NavTolanguage(showFrom: String){
        navSingleToTop(route= LanguageRoute(showFrom))
    }
    // dùng ở language
    fun navToOnboardScreen() {
        navSingleToTop(route = OnbroadRoute)
    }
    // dùng ở onbroad
    fun navToAuthenScreen() {
        navSingleToTop(route = AuthenRoute)
    }
    // dùng ở authen
    fun navToMainScreen() {
        navSingleToTop(route = MainRoute)
    }
}