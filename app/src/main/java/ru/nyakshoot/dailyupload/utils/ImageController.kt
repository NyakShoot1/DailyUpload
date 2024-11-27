package ru.nyakshoot.dailyupload.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun createImageFile(context: Context): Uri {
    val storageDir = File(context.getExternalFilesDir(null), "images")
    if (!storageDir.exists()) storageDir.mkdir()

    val file = File(storageDir, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

fun copyImageToAppStorage(context: Context, sourceUri: Uri): Uri? {
    return try {
        // Создаем директорию, если её нет
        val storageDir = File(context.getExternalFilesDir(null), "images")
        if (!storageDir.exists()) storageDir.mkdir()
        // Создаем новый файл с уникальным именем
        val destinationFile = File(storageDir, "photo_${System.currentTimeMillis()}.jpg")
        // Копируем содержимое
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        }
        // Создаем Uri через FileProvider
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            destinationFile
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}