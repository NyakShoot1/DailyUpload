package ru.nyakshoot.dailyupload.presentation.main.view.composables

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import ru.nyakshoot.dailyupload.R
import ru.nyakshoot.dailyupload.data.local.entity.PhotoEntity
import ru.nyakshoot.dailyupload.data.local.entity.UploadStatus
import ru.nyakshoot.dailyupload.utils.formatTimestamp

@Composable
fun PhotosGrid(
    photos: List<PhotoEntity>,
    modifier: Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        val groupedPhotos = photos.sortedByDescending { it.dateAdded }
            .groupBy { photo -> formatTimestamp(photo.dateAdded) }

        groupedPhotos.forEach { (date, photosForDate) ->
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                val status = if (photosForDate.any { it.uploadStatus == UploadStatus.FAILED }) {
                    UploadStatus.FAILED
                } else if (photos.all { it.uploadStatus == UploadStatus.PENDING }) {
                    UploadStatus.PENDING
                } else if (photos.all { it.uploadStatus == UploadStatus.UPLOADING }) {
                    UploadStatus.UPLOADING
                } else {
                    UploadStatus.SUCCESS
                }

                DateDivider(date, status, 3) {}
            }

            items(
                items = photosForDate,
                key = { it.uri.toString() }
            ) { photo ->
                PhotoGridItem(photoUri = photo.uri)
            }
        }
    }
}

@Composable
private fun PhotoGridItem(
    photoUri: Uri,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                // todo open photo full screen
            }
    ) {
        GlideImage(
            imageModel = { photoUri },
            requestBuilder = {
                Glide
                    .with(LocalContext.current)
                    .asBitmap()
                    .apply(
                        RequestOptions()
                            .override(300, 500)
                    )
                    .transition(withCrossFade())
            },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            modifier = Modifier.fillMaxSize(),
            failure = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_no_photography_24),
                        contentDescription = "error loading photo",
                        modifier = Modifier.size(48.dp),
                        tint = Color.DarkGray
                    )
                }
            }
        )
    }
}


