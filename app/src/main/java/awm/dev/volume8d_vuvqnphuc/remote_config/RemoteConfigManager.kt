package awm.dev.volume8d_vuvqnphuc.remote_config

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor() {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        val defaults = mapOf(
            "isCheckADS" to true,
            "banner_main" to "",
        )
        remoteConfig.setDefaultsAsync(defaults)
    }

    fun isCheckADS(): Boolean = remoteConfig.getBoolean("isCheckADS")

    fun fetchValues(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("RemoteConfig", "Fetch success: $updated")
                    onComplete(true)
                } else {
                    val exception = task.exception
                    Log.e("RemoteConfig", "Fetch failed: ${exception?.message}")
                    onComplete(false)
                }
            }
    }

    fun getBannerMain(): String {
        val adId = remoteConfig.getString("banner_main")
        Log.d("banner_main", "banner_main: $adId")
        return adId
    }
}