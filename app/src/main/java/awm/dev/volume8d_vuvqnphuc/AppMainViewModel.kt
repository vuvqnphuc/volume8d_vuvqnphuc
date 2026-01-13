package awm.dev.volume8d_vuvqnphuc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile
import awm.dev.volume8d_vuvqnphuc.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppMainViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _musicFiles = MutableStateFlow<List<MusicFile>>(emptyList())
    val musicFiles: StateFlow<List<MusicFile>> = _musicFiles.asStateFlow()

    fun loadMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            val files = musicRepository.getAllMusicFiles()
            _musicFiles.value = files
        }
    }

    fun removeMusic(music: MusicFile) {
        val currentList = _musicFiles.value.toMutableList()
        currentList.remove(music)
        _musicFiles.value = currentList
    }
}