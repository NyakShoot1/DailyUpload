package ru.nyakshoot.dailyupload.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PhotoApi {

    @Multipart
    @POST("upload")
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part
    ): UploadResponse // todo

    @Multipart
    @POST("upload/multiple")
    suspend fun uploadMultiplePhotos(
        @Part files: List<MultipartBody.Part>
    ): UploadResponse

}