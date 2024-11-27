package ru.nyakshoot.dailyupload.data.remote

import com.google.gson.annotations.SerializedName

data class UploadedFile(
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("stored_name")
    val storedName: String,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("size")
    val size: Int,
)