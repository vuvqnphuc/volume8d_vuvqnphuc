package awm.dev.volume8d_vuvqnphuc

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastFirstOrNull
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import awm.dev.volume8d_vuvqnphuc.data.local.Language
import awm.dev.volume8d_vuvqnphuc.ui.Instructions.InstructionsScreen
import awm.dev.volume8d_vuvqnphuc.ui.language.LanguageScreen
import awm.dev.volume8d_vuvqnphuc.ui.main.MainScreen
import awm.dev.volume8d_vuvqnphuc.ui.splash.SplashScreen
import awm.dev.volume8d_vuvqnphuc.utils.language.Constants.listLanguageApp
import awm.dev.volume8d_vuvqnphuc.utils.language.ManagerSaveLocal
import awm.dev.volume8d_vuvqnphuc.utils.language.changeLanguage
import awm.dev.volume8d_vuvqnphuc.utils.nav.BaseRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.InstructionsRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.LanguageRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.MainRoute
import awm.dev.volume8d_vuvqnphuc.utils.nav.NavAction
import awm.dev.volume8d_vuvqnphuc.utils.nav.SplashRoute

@Composable
fun AppMainNavHost(
    navController: NavHostController,
    navAction: NavAction,
    startRouter: BaseRoute,
    context: Activity,
) {
    val scopeNav = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = startRouter
    ) {
        composable<SplashRoute> {
            SplashScreen(
                nextScreen = {
                    if (ManagerSaveLocal.getFirstOpen()) {
                        navAction.navToLanguageScreen(showFrom = SplashRoute.toString())
                    } else {
                        navAction.navToMainScreen()
                    }
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
                    }

                    if (route.showFrom == SplashRoute.toString()) {
                        ManagerSaveLocal.setFirstOpen(false)
                        navController.popBackStack()
                        navAction.navToMainScreen()
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<MainRoute> {
            var selectedIndex by remember { mutableStateOf(0) }
            MainScreen(
                currentIndexTab = selectedIndex,
                changeIndexTab = { selectedIndex = it },
                onLanguageClick = {
                    navAction.navToLanguageScreen(showFrom = MainRoute.toString())
                },
                onShareClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "https://play.google.com/store/apps/details?id=${context.packageName}"
                        )
                        type = "text/plain"
                    }
                    context.startActivity(
                        Intent.createChooser(
                            sendIntent,
                            null
                        )
                    )
                },
                onRateClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${context.packageName}")
                    )
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                            )
                        )
                    }
                },
                onInstructionsClick = {
                    navAction.navToInstructionsScreen()
                }
            )
        }
        composable<InstructionsRoute> {
            InstructionsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}