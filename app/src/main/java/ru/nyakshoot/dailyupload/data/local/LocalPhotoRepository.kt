package ru.nyakshoot.dailyupload.data.local

import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity
import ru.nyakshoot.dailyupload.data.local.entity.UploadStatus

interface LocalPhotoRepository {

    suspend fun getPhotos(): List<PhotoEntity>

    suspend fun addPhoto(photo: PhotoEntity)

    suspend fun updatePhotoStatus(photo: PhotoEntity, status: UploadStatus)

    suspend fun updatePhotoStatusByFileName(fileName: String, status: UploadStatus)

    suspend fun getPhotosForToday(): List<PhotoEntity>

}