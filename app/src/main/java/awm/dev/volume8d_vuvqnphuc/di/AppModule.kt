package awm.dev.volume8d_vuvqnphuc.di

import android.content.Context
import androidx.room.Room
import awm.dev.volume8d_vuvqnphuc.data.local.AppDatabase
import awm.dev.volume8d_vuvqnphuc.data.local.MusicDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "volume8d_db"
        ).build()
    }

    @Provides
    fun provideMusicDao(database: AppDatabase): MusicDao {
        return database.musicDao()
    }
}
