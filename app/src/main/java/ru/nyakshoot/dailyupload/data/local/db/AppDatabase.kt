package ru.nyakshoot.dailyupload.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.nyakshoot.dailyupload.data.local.converter.UriConverter
import ru.nyakshoot.dailyupload.data.local.dao.PhotoDao
import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity

@Database(
    version = 1,
    entities = [
        PhotoEntity::class
    ]
)
@TypeConverters(UriConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        fun create(@ApplicationContext appContext: Context): AppDatabase =
            Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "photo_database"
            )
                .fallbackToDestructiveMigration()
                .build()
    }

}