package ru.nyakshoot.dailyupload.presentation.main.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.nyakshoot.dailyupload.presentation.main.view.composables.PhotosGrid
import ru.nyakshoot.dailyupload.presentation.main.viewmodel.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }

    val photos by viewModel.photos.collectAsState()

    when (cameraPermissionState.status) {
        is PermissionStatus.Granted -> {
            when (photos.isNotEmpty()) {
                true -> {
                    PhotosGrid(
                        photos = photos,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                false -> Unit
            }
        }

        is PermissionStatus.Denied -> {
            Text("No permission")
        }
    }

}