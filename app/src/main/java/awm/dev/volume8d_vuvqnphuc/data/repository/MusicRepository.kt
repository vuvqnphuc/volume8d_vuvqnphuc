package awm.dev.volume8d_vuvqnphuc.data.repository

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import awm.dev.volume8d_vuvqnphuc.R
import awm.dev.volume8d_vuvqnphuc.data.local.MusicDao
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed class DeleteResult {
    object Success : DeleteResult()
    data class RequiresPermission(val intentSender: IntentSender) : DeleteResult()
    data class Error(val message: String) : DeleteResult()
}

class MusicRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val musicDao: MusicDao
) {
    suspend fun getAllMusicFiles(): List<MusicFile> {
        val musicList = mutableListOf<MusicFile>()
        
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

        val dbFiles = musicDao.getAllMusicSync()

        val combined = dbFiles + mediaStoreFiles
        
        if (combined.isEmpty()) {
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
            name = context.getString(R.string.mikenco_trend_remix),
            duration = "05:48",
            path = path,
            isPreset = true
        )
        musicDao.insertMusic(preset)
    }

    suspend fun removeMusic(music: MusicFile): DeleteResult {
        musicDao.deleteMusic(music)

        if (!music.isPreset) {
            try {
                val uri = if (music.id > 0) {
                    android.content.ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        music.id
                    )
                } else {
                     null
                }

                if (uri != null) {
                    try {
                        context.contentResolver.delete(uri, null, null)
                    } catch (securityException: SecurityException) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            val deleteRequest = MediaStore.createDeleteRequest(
                                context.contentResolver,
                                listOf(uri)
                            ).intentSender
                            return DeleteResult.RequiresPermission(deleteRequest)
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            val recoverableSecurityException = securityException as? RecoverableSecurityException
                                ?: throw securityException
                            return DeleteResult.RequiresPermission(recoverableSecurityException.userAction.actionIntent.intentSender)
                        } else {
                            throw securityException
                        }
                    }
                } else {
                    val file = java.io.File(music.path)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return DeleteResult.Error(e.message ?: "Unknown error")
            }
        }

        val remainingMusic = getAllMusicFiles()
        if (remainingMusic.isEmpty()) {
            initializePreset()
        }
        
        return DeleteResult.Success
    }
}
