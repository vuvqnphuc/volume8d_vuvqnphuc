package awm.dev.volume8d_vuvqnphuc.data.repository

import android.content.Context
import android.provider.MediaStore
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MusicRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun getAllMusicFiles(): List<MusicFile> {
        val musicList = mutableListOf<MusicFile>()
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

        val selection = null
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        try {
            android.util.Log.d("MusicRepository", "Querying MediaStore...")
            context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)

                android.util.Log.d("MusicRepository", "Found ${cursor.count} items")

                while (cursor.moveToNext()) {
                    val id = if (idColumn != -1) cursor.getLong(idColumn) else 0L
                    val name = if (nameColumn != -1) cursor.getString(nameColumn)
                        ?: "Unknown" else "Unknown"
                    val durationMs =
                        if (durationColumn != -1) cursor.getLong(durationColumn) else 0L
                    val path = if (dataColumn != -1) cursor.getString(dataColumn) ?: "" else ""
                    if (path.isNotEmpty()) {
                        val durationStr = formatDuration(durationMs)
                        musicList.add(MusicFile(id, name, durationStr, path))
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MusicRepository", "Error querying music", e)
            e.printStackTrace()
        }

        return musicList
    }

    private fun formatDuration(durationMs: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
