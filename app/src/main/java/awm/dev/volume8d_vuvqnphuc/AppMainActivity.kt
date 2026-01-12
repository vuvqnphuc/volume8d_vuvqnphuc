package awm.dev.volume8d_vuvqnphuc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import awm.dev.volume8d_vuvqnphuc.ui.theme.Volume8d_vuvqnphucTheme
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

            AppMainNavHost(
                navController = navController,
                navAction = navAction,
                startRouter = SplashRoute,
                context = this
            )
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUiManager.applyImmersiveNavigation(this)
        }
    }
}