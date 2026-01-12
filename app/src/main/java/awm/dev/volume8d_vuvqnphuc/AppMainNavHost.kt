package awm.dev.volume8d_vuvqnphuc

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.fastFirstOrNull
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import awm.dev.volume8d_vuvqnphuc.data.local.Language
import awm.dev.volume8d_vuvqnphuc.ui.language.LanguageScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.MainScreen
import awm.dev.volume8d_vuvqnphuc.ui.splash.SplashScreen
import awm.dev.volume8d_vuvqnphuc.utils.nav.BaseRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.LanguageRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.MainRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.NavAction
import awm.dev.volume8d_vuvqnphuc.utils.nav.SplashRoute
import com.example.datatransfer.utils.language.Constants.listLanguageApp
import com.example.datatransfer.utils.language.ManagerSaveLocal
import com.example.datatransfer.utils.language.changeLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppMainNavHost(
    navController: NavHostController,
    navAction: NavAction,
    startRouter: BaseRoute,
    context: Activity,
    appViewModel: AppMainViewModel = hiltViewModel<AppMainViewModel>(),
) {
    val scopeNav = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = startRouter
    ) {
        composable<SplashRoute> {
            SplashScreen(
                nextScreen = {
                    navAction.navToLanguageScreen(showFrom = SplashRoute.toString())
                }
            )
        }
        composable<LanguageRoute> {
            val route = it.toRoute<LanguageRoute>()
            val currentLanguage by remember {
                mutableStateOf(ManagerSaveLocal.getLanguageApp())
            }

            val itemDefault by remember(currentLanguage) {
                derivedStateOf {
                    currentLanguage?.let { lang ->
                        listLanguageApp.fastFirstOrNull { it.code.name == lang.name }
                    }
                }
            }
            val mList: List<Language> = remember(itemDefault) {
                itemDefault?.let { selected ->
                    listOf(selected) + listLanguageApp.filterNot { it.id == selected.id }
                } ?: listLanguageApp
            }

            LanguageScreen(
                listLanguage = mList,
                language = itemDefault,
                onChangeLanguage = { lang ->
                    lang?.let {
                        changeLanguage(it)
                        scopeNav.launch(Dispatchers.IO) {
                            ManagerSaveLocal.setLanguageApp(lang)
                        }
                    }

                    if (route.showFrom == SplashRoute.toString()) {
                        navController.popBackStack()
                        navAction.navToMainScreen()
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<MainRoute> {
            MainScreen()
        }
    }
}