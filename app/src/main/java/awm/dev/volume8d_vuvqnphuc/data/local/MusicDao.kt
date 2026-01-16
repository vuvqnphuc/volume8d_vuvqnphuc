package awm.dev.volume8d_vuvqnphuc.data.local

import androidx.room.*
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {
    @Query("SELECT * FROM music_files")
    fun getAllMusic(): Flow<List<MusicFile>>

    @Query("SELECT * FROM music_files")
    suspend fun getAllMusicSync(): List<MusicFile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: MusicFile)

    @Delete
    suspend fun deleteMusic(music: MusicFile)

    @Query("SELECT COUNT(*) FROM music_files WHERE isPreset = 1")
    suspend fun getPresetCount(): Int
}
