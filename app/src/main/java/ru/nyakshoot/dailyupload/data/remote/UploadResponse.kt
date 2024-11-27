package ru.nyakshoot.dailyupload.data.remote

import com.google.gson.annotations.SerializedName

data class UploadResponse(
    @SerializedName("uploaded_files")
    val uploadedFiles: List<UploadedFile>,
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("total_uploaded")
    val totalUploaded: Int,
    @SerializedName("total_failed")
    val totalFailed: Int,
    @SerializedName("bucket")
    val bucket: String
)