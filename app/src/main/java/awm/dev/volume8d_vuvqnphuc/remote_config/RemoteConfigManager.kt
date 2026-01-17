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
            // OPEN APP
            "open_app" to "",
            // BANNER
            "banner_language" to "",
            "banner_music" to "",
            "banner_list_music" to "",
            "banner_volume" to "",
            "banner_setting" to "",
            // INTER
            "inter_list_music_menu" to "",
            "inter_done_language" to ""
        )
        remoteConfig.setDefaultsAsync(defaults)
    }

    fun isCheckADS(): Boolean = remoteConfig.getBoolean("isCheckADS")

    fun fetchValues(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("RemoteConfig", "✅ KẾT NỐI FIREBASE THÀNH CÔNG")
                    onComplete(true)
                } else {
                    val exception = task.exception
                    Log.e("RemoteConfig", "⛔  KẾT NỐI FIREBASE THẤT BẠI : ${exception?.message}")
                    onComplete(false)
                }
            }
    }

    // BANNER
    fun setBannerLanguage(): String {
        val BannerLanguage = remoteConfig.getString("banner_language")
        Log.e("log1", " : BannerLanguage ")
        return BannerLanguage
    }
    fun setBannerMusic(): String {
        val BannerMusic = remoteConfig.getString("banner_music")
        Log.e("log1", " : BannerMusic ")
        return BannerMusic
    }

    fun setBannerListMusic(): String {
        val BannerListMusic = remoteConfig.getString("banner_list_music")
        Log.e("log1", " : BannerListMusic ")
        return BannerListMusic
    }

    fun setBannerVolume(): String {
        val BannerVolume = remoteConfig.getString("banner_volume")
        Log.e("log1", " : BannerVolume ")
        return BannerVolume
    }

    fun setBannerSetting(): String {
        val BannerSetting = remoteConfig.getString("banner_setting")
        Log.e("log1", " : BannerSetting")
        return BannerSetting
    }

    // INTER
    fun setInterListMusicMenu(): String {
        val InterListMusicMenu = remoteConfig.getString("inter_list_music_menu")
        Log.e("log1", " : InterListMusicMenu ")
        return InterListMusicMenu
    }

    fun setInterDoneLanguage(): String {
        val InterDoneLanguage = remoteConfig.getString("inter_done_language")
        Log.e("log1", " : InterDoneLanguage ")
        return InterDoneLanguage
    }

    fun setOpenApp(): String {
        val openApp = remoteConfig.getString("open_app")
        Log.e("log1", " : openApp ")
        return openApp
    }
}