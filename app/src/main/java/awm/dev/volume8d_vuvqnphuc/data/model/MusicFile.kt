package awm.dev.volume8d_vuvqnphuc.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.Keep

@Keep
@Entity(tableName = "music_files")
data class MusicFile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val duration: String,
    val path: String,
    val isPreset: Boolean = false
)
