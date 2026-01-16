package awm.dev.volume8d_vuvqnphuc.data.model

import androidx.annotation.Keep

@Keep
data class MusicFile(
    val id: Long,
    val name: String,
    val duration: String,
    val path: String
)
