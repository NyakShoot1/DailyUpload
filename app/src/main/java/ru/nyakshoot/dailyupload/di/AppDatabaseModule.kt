package ru.nyakshoot.dailyupload.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nyakshoot.dailyupload.data.local.dao.PhotoDao
import ru.nyakshoot.dailyupload.data.local.db.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase = AppDatabase.create(appContext)

    @Provides
    fun providePhotoDao(appDatabase: AppDatabase): PhotoDao = appDatabase.photoDao()

}