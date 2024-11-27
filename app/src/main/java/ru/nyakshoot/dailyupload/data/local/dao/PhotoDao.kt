package ru.nyakshoot.dailyupload.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity
import ru.nyakshoot.dailyupload.data.local.entity.UploadStatus

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: PhotoEntity)

    @Transaction
    @Query("SELECT * FROM photo")
    suspend fun getPhotos(): List<PhotoEntity>

    @Update
    suspend fun updatePhotoStatus(photo: PhotoEntity)

    @Query("UPDATE photo SET uploadStatus = :status WHERE fileName = :fileName")
    suspend fun updatePhotoStatusByFileName(fileName: String, status: UploadStatus)

    @Query("SELECT * FROM photo WHERE dateAdded BETWEEN :startOfDay AND :endOfDay")
    suspend fun getPhotosForToday(startOfDay: Long, endOfDay: Long): List<PhotoEntity>

}