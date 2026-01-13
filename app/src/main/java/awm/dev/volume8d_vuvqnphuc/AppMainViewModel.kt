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

    // Effects State
    private val _masterVolume = MutableStateFlow(1.0f)
    val masterVolume: StateFlow<Float> = _masterVolume.asStateFlow()

    private val _bassStrength = MutableStateFlow(0.0f)
    val bassStrength: StateFlow<Float> = _bassStrength.asStateFlow()

    private val _vocalStrength = MutableStateFlow(0.0f)
    val vocalStrength: StateFlow<Float> = _vocalStrength.asStateFlow()

    private val _is8DEnabled = MutableStateFlow(false)
    val is8DEnabled: StateFlow<Boolean> = _is8DEnabled.asStateFlow()

    private val _surroundStrength = MutableStateFlow(0.3f) // Speed of 8D
    val surroundStrength: StateFlow<Float> = _surroundStrength.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private var effect8DJob: Job? = null

    // Audio Effects
    private var bassBoost: android.media.audiofx.BassBoost? = null
    private var equalizer: android.media.audiofx.Equalizer? = null
    private var loudnessEnhancer: android.media.audiofx.LoudnessEnhancer? = null

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
            releaseEffects()
            mediaPlayer?.release()
            mediaPlayer = null
            progressJob?.cancel()

            mediaPlayer = MediaPlayer().apply {
                setDataSource(music.path)
                prepare()
                
                // Init Effects
                initAudioEffects(audioSessionId)

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
            
            // Apply current settings
            applyVolume(_masterVolume.value)
            applyBass(_bassStrength.value)
            applyVocal(_vocalStrength.value)
            apply8D(_is8DEnabled.value)

            startProgressUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAudioEffects(sessionId: Int) {
        try {
            bassBoost = android.media.audiofx.BassBoost(0, sessionId).apply {
                enabled = true
            }
            equalizer = android.media.audiofx.Equalizer(0, sessionId).apply {
                enabled = true
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                loudnessEnhancer = android.media.audiofx.LoudnessEnhancer(sessionId).apply {
                    enabled = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releaseEffects() {
        try {
            bassBoost?.release()
            equalizer?.release()
            loudnessEnhancer?.release()
            bassBoost = null
            equalizer = null
            loudnessEnhancer = null
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
                effect8DJob?.cancel() // Pause 8D effect
            } else {
                player.start()
                _isPlaying.value = true
                startProgressUpdate()
                if (_is8DEnabled.value) start8DEffect() // Resume 8D effect
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

    // --- Audio Effects Control ---

    fun setMasterVolume(value: Float) {
        _masterVolume.value = value
        applyVolume(value)
    }

    private fun applyVolume(value: Float) {
        val player = mediaPlayer ?: return
        if (value <= 1.0f) {
            player.setVolume(value, value)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                loudnessEnhancer?.setTargetGain(0)
            }
        } else {
            player.setVolume(1.0f, 1.0f)
            // Boost > 1.0. Max boost usually around 10-20dB?
            // value 1.0 -> 2.0. Map to 0 -> 2000mB (20dB) for example.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                val gain = ((value - 1.0f) * 1000).toInt() // Example mapping
                loudnessEnhancer?.setTargetGain(gain)
            }
        }
    }

    fun setBassStrength(value: Float) {
         _bassStrength.value = value
        applyBass(value)
    }

    private fun applyBass(value: Float) {
        bassBoost?.let {
            if (it.strengthSupported) {
                // strength is 0 to 1000
                it.setStrength((value * 1000).toInt().toShort())
            }
        }
    }

    fun setVocalStrength(value: Float) {
        _vocalStrength.value = value
        applyVocal(value)
    }

    private fun applyVocal(value: Float) {
        // Boost Mid/High freqs (1kHz - 4kHz range)
        equalizer?.let { eq ->
            val bands = eq.numberOfBands
            for (i in 0 until bands) {
                val centerFreq = eq.getCenterFreq(i.toShort()) / 1000 // Hz
                // Vocal range roughly 500Hz to 4kHz
                if (centerFreq in 500..4000) {
                     val range = eq.bandLevelRange // [min, max]
                     val max = range[1]
                     val boost = (max * value).toInt().toShort()
                     eq.setBandLevel(i.toShort(), boost)
                }
            }
        }
    }

    fun set8DEnabled(enabled: Boolean) {
        _is8DEnabled.value = enabled
        apply8D(enabled)
    }
    
    fun setSurroundStrength(value: Float) {
        _surroundStrength.value = value
        // Restart 8D to apply speed change if active
        if (_is8DEnabled.value) {
            apply8D(true)
        }
    }

    private fun apply8D(enabled: Boolean) {
        effect8DJob?.cancel()
        if (enabled && _isPlaying.value) {
            start8DEffect()
        } else {
             // Reset volume balance
             applyVolume(_masterVolume.value)
        }
    }

    private fun start8DEffect() {
        effect8DJob?.cancel()
        effect8DJob = viewModelScope.launch {
            var angle = 0.0
            while (isActive) {
                // Calculate Panning
                // Simple Sin/Cos panning
                val left = (Math.sin(angle) + 1) / 2.0 // 0 to 1
                val right = (Math.cos(angle) + 1) / 2.0 // 0 to 1
                
                // Adjust based on speed (_surroundStrength)
                // Default speed increment?
                val speed = _surroundStrength.value * 0.1 + 0.05
                angle += speed
                
                mediaPlayer?.let { player ->
                     // Scale by master volume
                     val vol = if (_masterVolume.value > 1f) 1f else _masterVolume.value
                     player.setVolume((left * vol).toFloat(), (right * vol).toFloat())
                }
                
                delay(50) // Update every 50ms
            }
        }
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
        releaseEffects()
        mediaPlayer?.release()
        mediaPlayer = null
        progressJob?.cancel()
        effect8DJob?.cancel()
    }
}