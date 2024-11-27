package ru.nyakshoot.dailyupload.data.local.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uri: Uri,
    val fileName: String,
    val dateAdded: Long = System.currentTimeMillis(),
    val uploadStatus: UploadStatus = UploadStatus.PENDING
)

enum class UploadStatus {
    PENDING,
    UPLOADING,
    SUCCESS,
    FAILED
}
