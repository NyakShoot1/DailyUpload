package ru.nyakshoot.dailyupload.presentation.main.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nyakshoot.dailyupload.data.local.LocalPhotoRepository
import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity
import ru.nyakshoot.dailyupload.data.remote.RemotePhotoRepository
import ru.nyakshoot.dailyupload.utils.getFileNameFromUri
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localPhotoRepository: LocalPhotoRepository,
    private val remotePhotoRepository: RemotePhotoRepository
) : ViewModel() {

    private val _photos = mutableStateListOf<PhotoEntity>()
    val photos: List<PhotoEntity> get() = _photos

    init {
        viewModelScope.launch {
            _photos.clear()
            _photos.addAll(localPhotoRepository.getPhotos())
        }
    }

    fun addPhoto(context: Context, photoUri: Uri) = viewModelScope.launch {
        val newPhoto =
            PhotoEntity(uri = photoUri, fileName = getFileNameFromUri(context, photoUri)!!)
        _photos.add(newPhoto)
        localPhotoRepository.addPhoto(newPhoto)
    }
}