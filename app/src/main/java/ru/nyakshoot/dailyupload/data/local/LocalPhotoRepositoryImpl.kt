package ru.nyakshoot.dailyupload.data.local

import ru.nyakshoot.dailyupload.data.local.dao.PhotoDao
import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity
import ru.nyakshoot.dailyupload.data.local.entity.UploadStatus
import ru.nyakshoot.dailyupload.utils.getTodayTimestamps
import javax.inject.Inject

class LocalPhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : LocalPhotoRepository {

    override suspend fun getPhotos(): List<PhotoEntity> {
        return photoDao.getPhotos()
    }

    override suspend fun addPhoto(photo: PhotoEntity) {
        photoDao.insert(PhotoEntity(uri = photo.uri, fileName = photo.fileName))
    }

    override suspend fun updatePhotoStatus(photo: PhotoEntity, status: UploadStatus) {
        photoDao.updatePhotoStatus(photo.copy(uploadStatus = status))
    }

    override suspend fun updatePhotoStatusByFileName(fileName: String, status: UploadStatus) {
        photoDao.updatePhotoStatusByFileName(fileName, status)
    }

    override suspend fun getPhotosForToday(): List<PhotoEntity> {
        val (startOfDay, endOfDay) = getTodayTimestamps()
        return photoDao.getPhotosForToday(startOfDay, endOfDay)
    }

}