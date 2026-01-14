package awm.dev.volume8d_vuvqnphuc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import awm.dev.volume8d_vuvqnphuc.component.DialogCheckInternet
import awm.dev.volume8d_vuvqnphuc.utils.internet.ConnectivityObserver
import awm.dev.volume8d_vuvqnphuc.utils.nav.NavAction
import awm.dev.volume8d_vuvqnphuc.utils.nav.SplashRoute
import awm.dev.volume8d_vuvqnphuc.utils.system.SystemUiManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navAction = remember(navController) { NavAction(navController) }

            val appViewModel: AppMainViewModel = hiltViewModel()
            val networkStatus by appViewModel.networkStatus.collectAsState(initial = ConnectivityObserver.Status.Available)

            AppMainNavHost(
                navController = navController,
                navAction = navAction,
                startRouter = SplashRoute,
                context = this
            )

            if (networkStatus != ConnectivityObserver.Status.Available) {
                DialogCheckInternet(
                    onDismissRequest = {},
                    onTryAgain = {
                        appViewModel.refreshNetworkStatus()
                    },
                    onGoToSetting = {
                        startActivity(android.content.Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
                    }
                )
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUiManager.applyImmersiveNavigation(this)
        }
    }
}