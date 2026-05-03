package com.wakeupman.core.di

import android.content.Context
import androidx.room.Room
import com.wakeupman.data.local.AppDatabase
import com.wakeupman.data.local.IncidentDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "wakeupman_db"
        ).build()
    }

    @Provides
    fun provideIncidentDao(database: AppDatabase): IncidentDao {
        return database.incidentDao()
    }
}
