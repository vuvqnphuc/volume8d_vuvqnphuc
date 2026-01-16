package awm.dev.volume8d_vuvqnphuc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import awm.dev.volume8d_vuvqnphuc.data.model.MusicFile

@Database(entities = [MusicFile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}
