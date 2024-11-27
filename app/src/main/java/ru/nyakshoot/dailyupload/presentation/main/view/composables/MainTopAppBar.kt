package ru.nyakshoot.dailyupload.presentation.main.view.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.nyakshoot.dailyupload.R
import ru.nyakshoot.dailyupload.presentation.main.viewmodel.MainViewModel
import ru.nyakshoot.dailyupload.utils.copyImageToAppStorage
import ru.nyakshoot.dailyupload.utils.createImageFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val photoUri = remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri.value?.let { uri ->
                viewModel.addPhoto(context, uri)
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { sourceUri ->
            copyImageToAppStorage(context, sourceUri)?.let { destinationUri ->
                viewModel.addPhoto(context, destinationUri)
            }
        }
    }

    Column {
        TopAppBar(
            title = { Text("UploadDaily") },
            actions = {
                IconButton(
                    onClick = {
                        val uri = createImageFile(context)
                        photoUri.value = uri
                        takePictureLauncher.launch(uri)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_a_photo_24),
                        contentDescription = "Take a picture"
                    )
                }
                IconButton(
                    onClick = {
                        pickImageLauncher.launch("image/*")
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_photo_alternate_24),
                        contentDescription = "Add photo from gallery"
                    )
                }
            }
        )
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
    }

}