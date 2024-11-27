package ru.nyakshoot.dailyupload.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File

fun uriToFile(context: Context, uri: Uri): File {
    val contentResolver = context.contentResolver
    val tempFile = File(context.cacheDir, "${getFileNameFromUri(context, uri)}.jpg")

    contentResolver.openInputStream(uri)?.use { inputStream ->
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    Log.d("test", tempFile.toString())
    return tempFile
}

fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val contentResolver = context.contentResolver

    // Пытаемся запросить данные через ContentResolver
    val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
    contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = cursor.getString(nameIndex)
            }
        }
    }

    return fileName
}
