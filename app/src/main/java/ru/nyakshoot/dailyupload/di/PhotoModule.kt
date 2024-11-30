package ru.nyakshoot.dailyupload.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nyakshoot.dailyupload.data.local.LocalPhotoRepository
import ru.nyakshoot.dailyupload.data.local.LocalPhotoRepositoryImpl
import ru.nyakshoot.dailyupload.data.remote.RemotePhotoRepository
import ru.nyakshoot.dailyupload.data.remote.RemotePhotoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PhotoModule {

    @Binds
    @Singleton
    fun bindPhotoRepository(
        impl: LocalPhotoRepositoryImpl,
    ): LocalPhotoRepository

    @Binds
    @Singleton
    fun bindRemotePhotoRepository(
        impl: RemotePhotoRepositoryImpl
    ): RemotePhotoRepository

}