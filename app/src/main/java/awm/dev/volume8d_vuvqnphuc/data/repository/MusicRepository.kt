package awm.dev.volume8d_vuvqnphuc.data.repository

import android.content.Context
import android.provider.MediaStore
import awm.dev.volume8d_vuvqnphuc.R
import awm.dev.volume8d_vuvqnphuc.data.local.MusicDao
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MusicRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicDao: MusicDao
) {
    suspend fun getAllMusicFiles(): List<MusicFile> {
        val musicList = mutableListOf<MusicFile>()
        
        // 1. Get files from MediaStore
        val mediaStoreFiles = mutableListOf<MusicFile>()
        val collection = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        try {
            context.contentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

                while (cursor.moveToNext()) {
                    val id = if (idColumn != -1) cursor.getLong(idColumn) else 0L
                    val name = if (nameColumn != -1) cursor.getString(nameColumn) ?: "Unknown" else "Unknown"
                    val durationMs = if (durationColumn != -1) cursor.getLong(durationColumn) else 0L
                    val path = if (dataColumn != -1) cursor.getString(dataColumn) ?: "" else ""
                    if (path.isNotEmpty()) {
                        val durationStr = formatDuration(durationMs)
                        mediaStoreFiles.add(MusicFile(id, name, durationStr, path))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Get files from Room DB
        val dbFiles = musicDao.getAllMusicSync()

        // 3. Combine and check if empty
        val combined = dbFiles + mediaStoreFiles
        
        if (combined.isEmpty()) {
            // Recreate preset if NO files found anywhere
            initializePreset()
            return musicDao.getAllMusicSync()
        }

        return combined
    }

    private fun formatDuration(durationMs: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private suspend fun initializePreset() {
        val path = "android.resource://${context.packageName}/raw/file_music"
        val preset = MusicFile(
            name = "VIETNAM MASHAP",
            duration = "05:48",
            path = path,
            isPreset = true
        )
        musicDao.insertMusic(preset)
    }

    suspend fun removeMusic(music: MusicFile) {
        // Delete from Room DB if it exists there
        musicDao.deleteMusic(music)

        // If it's not a preset, delete from device
        if (!music.isPreset) {
            try {
                // Delete from filesystem
                val file = java.io.File(music.path)
                if (file.exists()) {
                    file.delete()
                }

                // Delete from MediaStore to update system database
                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val selection = "${MediaStore.Audio.Media.DATA} = ?"
                val selectionArgs = arrayOf(music.path)
                context.contentResolver.delete(uri, selection, selectionArgs)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Check if anything is left. If not, initializePreset will be called in the next load.
        // Or we can just call it here if we want immediate effect.
        // Since loadMusic in ViewModel calls getAllMusicFiles, and that check is already there,
        // it should be fine. But let's be explicit if we want to return the updated list.
    }
}
