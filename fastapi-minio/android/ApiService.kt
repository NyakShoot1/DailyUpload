import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Response data class for file upload
 */
data class UploadResponse(
    val message: String,
    val filename: String,
    val bucket: String
)

/**
 * Main API interface for the FastAPI-MinIO service
 */
interface ApiService {
    /**
     * Upload a single photo
     * @param file MultipartBody.Part containing the image file
     */
    @Multipart
    @POST("upload")
    suspend fun uploadPhoto(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    /**
     * Upload multiple photos
     * @param files List of MultipartBody.Part containing the image files
     */
    @Multipart
    @POST("upload/multiple")
    suspend fun uploadMultiplePhotos(
        @Part files: List<MultipartBody.Part>
    ): Response<List<UploadResponse>>

    /**
     * List all files in the bucket
     */
    @GET("files")
    suspend fun listFiles(): Response<List<String>>

    /**
     * Delete a file by filename
     * @param filename Name of the file to delete
     */
    @DELETE("files/{filename}")
    suspend fun deleteFile(
        @Path("filename") filename: String
    ): Response<Unit>
}

// Example of how to create the Retrofit instance
/*
val retrofit = Retrofit.Builder()
    .baseUrl("YOUR_BASE_URL")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)
*/

// Example of how to use the API
/*
// Single file upload
val file = File("path_to_file")
val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
val response = apiService.uploadPhoto(body)

// Multiple files upload
val files = listOf(file1, file2).map { file ->
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    MultipartBody.Part.createFormData("files", file.name, requestFile)
}
val response = apiService.uploadMultiplePhotos(files)

// List files
val files = apiService.listFiles()

// Delete file
val response = apiService.deleteFile("filename.jpg")
*/
