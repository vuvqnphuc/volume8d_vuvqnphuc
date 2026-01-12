package awm.dev.volume8d_vuvqnphuc.utils.nav

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data object SplashRoute : BaseRoute()

@Keep
@Serializable
data class LanguageRoute(val showFrom: String) : BaseRoute()

@Keep
@Serializable
data object MainRoute : BaseRoute()