package ru.nyakshoot.dailyupload.data.remote

import okhttp3.MultipartBody

interface RemotePhotoRepository {

    suspend fun uploadPhotos(files: List<MultipartBody.Part>): UploadResponse

    suspend fun uploadPhoto(file: MultipartBody.Part): UploadResponse

}