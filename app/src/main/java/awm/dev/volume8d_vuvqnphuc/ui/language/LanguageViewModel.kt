package awm.dev.volume8d_vuvqnphuc.ui.language

import androidx.lifecycle.ViewModel
import awm.dev.volume8d_vuvqnphuc.data.local.LANG
import awm.dev.volume8d_vuvqnphuc.data.local.Language
import awm.dev.volume8d_vuvqnphuc.utils.language.ManagerSaveLocal
import awm.dev.volume8d_vuvqnphuc.utils.language.changeLanguage
import awm.dev.volume8d_vuvqnphuc.utils.language.getLanguageFlag
import awm.dev.volume8d_vuvqnphuc.utils.language.getLanguageName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor() : ViewModel() {

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages: StateFlow<List<Language>> = _languages.asStateFlow()

    private val _selectedLanguage = MutableStateFlow<LANG>(LANG.EN)
    val selectedLanguage: StateFlow<LANG> = _selectedLanguage.asStateFlow()

    init {
        loadLanguages()
    }

    private fun loadLanguages() {
        val currentLang = ManagerSaveLocal.getLanguageApp() ?: LANG.EN
        _selectedLanguage.value = currentLang

        val allLanguages = LANG.values().mapIndexed { index, lang ->
            Language(
                id = index,
                name = getLanguageName(lang),
                code = lang,
                selected = lang == currentLang,
                icon = getLanguageFlag(lang)
            )
        }

        _languages.value = allLanguages
    }

    fun selectLanguage(language: Language) {
        _selectedLanguage.value = language.code

        // Update the list with new selection
        _languages.value = _languages.value.map { lang ->
            lang.copy(selected = lang.code == language.code)
        }

        // Save and apply language change
        changeLanguage(language.code)
    }
}
