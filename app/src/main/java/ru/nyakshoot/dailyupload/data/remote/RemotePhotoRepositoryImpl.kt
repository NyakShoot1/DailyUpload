package ru.nyakshoot.dailyupload.data.remote

import okhttp3.MultipartBody
import javax.inject.Inject

class RemotePhotoRepositoryImpl @Inject constructor(
    private val photoApi: PhotoApi
) : RemotePhotoRepository {

    override suspend fun uploadPhotos(files: List<MultipartBody.Part>): UploadResponse {
       return photoApi.uploadMultiplePhotos(files) // todo error handle
    }

    override suspend fun uploadPhoto(file: MultipartBody.Part): UploadResponse {
        return photoApi.uploadPhoto(file)
    }

}