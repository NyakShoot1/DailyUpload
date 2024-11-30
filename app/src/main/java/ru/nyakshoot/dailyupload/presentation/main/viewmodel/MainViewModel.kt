package ru.nyakshoot.dailyupload.presentation.main.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nyakshoot.dailyupload.data.local.LocalPhotoRepository
import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity
import ru.nyakshoot.dailyupload.utils.getFileNameFromUri
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localPhotoRepository: LocalPhotoRepository,
) : ViewModel() {

    private val _photos = MutableStateFlow<List<PhotoEntity>>(listOf())
    val photos: StateFlow<List<PhotoEntity>> = _photos.asStateFlow()

    init {
        viewModelScope.launch {
            _photos.value = localPhotoRepository.getPhotos()
        }
    }

    fun addPhoto(context: Context, photoUri: Uri) = viewModelScope.launch {
        val newPhoto = PhotoEntity(
            uri = photoUri,
            fileName = getFileNameFromUri(context, photoUri)!!
        )
        localPhotoRepository.addPhoto(newPhoto)
        _photos.update { currentPhotos -> currentPhotos + newPhoto }
    }

}