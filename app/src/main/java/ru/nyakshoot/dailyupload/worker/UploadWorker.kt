package ru.nyakshoot.dailyupload.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.nyakshoot.dailyupload.data.local.LocalPhotoRepository
import ru.nyakshoot.dailyupload.data.local.entity.UploadStatus
import ru.nyakshoot.dailyupload.data.remote.RemotePhotoRepository
import ru.nyakshoot.dailyupload.utils.uriToFile

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localPhotoRepository: LocalPhotoRepository,
    private val remotePhotoRepository: RemotePhotoRepository,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "UploadWorker"
    }

    override suspend fun doWork(): Result {
        val todayPhotos = localPhotoRepository.getPhotosForToday()
        if (todayPhotos.isEmpty()) {
            Log.d("UploadWorker", "No photos to upload")
            return Result.success()
        }

        todayPhotos.forEach { photo ->
            localPhotoRepository.updatePhotoStatusByFileName(photo.fileName, UploadStatus.UPLOADING)
        }

        val multipartPhotos = todayPhotos.map { photo ->
            val file = uriToFile(applicationContext, photo.uri)
            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            MultipartBody.Part.createFormData("files", file.name, requestFile)
        }

        return try {
            val response = remotePhotoRepository.uploadPhotos(multipartPhotos)

            response.uploadedFiles.forEach { photo ->
                localPhotoRepository.updatePhotoStatusByFileName(
                    photo.originalName,
                    UploadStatus.SUCCESS
                )
            }

            if (response.errors.isNotEmpty()){
                response.errors.forEach {  photoName ->
                    localPhotoRepository.updatePhotoStatusByFileName(photoName, UploadStatus.FAILED)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("UploadWorker", "Upload failed", e)

            todayPhotos.forEach { photo ->
                localPhotoRepository.updatePhotoStatusByFileName(
                    photo.fileName,
                    UploadStatus.FAILED
                )
            }

            Result.failure()
        }
    }
}