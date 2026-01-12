package awm.dev.volume8d_vuvqnphuc

import android.app.Application
import android.util.Log
import awm.dev.volume8d_vuvqnphuc.data.local.LANG
import com.example.datatransfer.utils.language.ManagerSaveLocal
import com.example.datatransfer.utils.language.changeLanguage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppMainHiltAndroid : Application() {

    override fun onCreate() {
        super.onCreate()
        ManagerSaveLocal.init(this)
        checkAndSetLanguageDefault()
    }

    private fun checkAndSetLanguageDefault() {
        val language = ManagerSaveLocal.getLanguageApp()
        Log.e("TAG", "checkAndSetLanguageDefault: $language")
        if (language == null) {
            val configuration = this@AppMainHiltAndroid.resources.configuration

            @Suppress("DEPRECATION")
            val locale = configuration.locale
            ManagerSaveLocal.setLanguageApp(
                when {
                    locale.language == "zh" && locale.country.equals(
                        "CN",
                        true
                    ) -> LANG.ZH   // Trung Quốc (简体)
                    locale.language == "zh" && locale.country.equals(
                        "TW",
                        true
                    ) -> LANG.TW // Đài Loan (繁體)
                    locale.language == "pt" && locale.country.equals("BR", true) -> LANG.BR
                    locale.language == "pt" && locale.country.equals("PT", true) -> LANG.PT

                    locale.language == "ar" -> LANG.AR
                    locale.language == "tr" -> LANG.TR
                    locale.language == "vi" -> LANG.VI
                    locale.language == "th" -> LANG.TH
                    locale.language == "ru" -> LANG.RU
                    locale.language == "pl" -> LANG.PL
                    locale.language == "ko" -> LANG.KO
                    locale.language == "ja" -> LANG.JA
                    locale.language == "it" -> LANG.IT
                    locale.language == "de" -> LANG.DE
                    locale.language == "cs" -> LANG.CS
                    locale.language == "hi" -> LANG.HI
                    locale.language == "fr" -> LANG.FR
                    locale.language == "in" -> LANG.IN
                    locale.language == "ms" -> LANG.MS
                    locale.language == "phi" -> LANG.PHI
                    locale.language == "es" -> LANG.ES
                    else -> LANG.EN
                }
            )
        } else {
            changeLanguage(language)
        }
    }
}