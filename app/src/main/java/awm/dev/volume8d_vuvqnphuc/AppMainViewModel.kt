package awm.dev.volume8d_vuvqnphuc
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile
import awm.dev.volume8d_vuvqnphuc.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltViewModel
class AppMainViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _musicFiles = MutableStateFlow<List<MusicFile>>(emptyList())
    val musicFiles: StateFlow<List<MusicFile>> = _musicFiles.asStateFlow()

    private val _currentMusic = MutableStateFlow<MusicFile?>(null)
    val currentMusic: StateFlow<MusicFile?> = _currentMusic.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0f)
    val currentPosition: StateFlow<Float> = _currentPosition.asStateFlow()

    private val _isRepeatOne = MutableStateFlow(false)
    val isRepeatOne: StateFlow<Boolean> = _isRepeatOne.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

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

    fun playMusic(music: MusicFile) {
        if (_currentMusic.value == music && mediaPlayer != null) {
            togglePlayPause()
            return
        }

        playNewMusic(music)
    }

    private fun playNewMusic(music: MusicFile) {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            progressJob?.cancel()

            mediaPlayer = MediaPlayer().apply {
                setDataSource(music.path)
                prepare()
                start()
                setOnCompletionListener {
                    if (_isRepeatOne.value) {
                        start()
                    } else {
                        nextSong()
                    }
                }
            }

            _currentMusic.value = music
            _isPlaying.value = true
            startProgressUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _isPlaying.value = false
                progressJob?.cancel()
            } else {
                player.start()
                _isPlaying.value = true
                startProgressUpdate()
            }
        }
    }

    fun nextSong() {
        val currentList = _musicFiles.value
        if (currentList.isEmpty()) return

        val currentIndex = currentList.indexOf(_currentMusic.value)
        val nextIndex = if (currentIndex < currentList.lastIndex) currentIndex + 1 else 0
        playNewMusic(currentList[nextIndex])
    }

    fun previousSong() {
        val currentList = _musicFiles.value
        if (currentList.isEmpty()) return

        val currentIndex = currentList.indexOf(_currentMusic.value)
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else currentList.lastIndex
        playNewMusic(currentList[prevIndex])
    }

    fun seekTo(position: Float) {
        mediaPlayer?.let { player ->
            val duration = player.duration
            val newPosition = (position * duration).toInt()
            player.seekTo(newPosition)
            _currentPosition.value = position
        }
    }

    fun toggleRepeat() {
        _isRepeatOne.value = !_isRepeatOne.value
    }

    private fun startProgressUpdate() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive && _isPlaying.value) {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        val progress = player.currentPosition.toFloat() / player.duration.toFloat()
                        _currentPosition.value = progress
                    }
                }
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        progressJob?.cancel()
    }
}