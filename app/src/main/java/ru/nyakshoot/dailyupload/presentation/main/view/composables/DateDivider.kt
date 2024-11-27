package ru.nyakshoot.dailyupload.presentation.main.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.nyakshoot.dailyupload.R
import ru.nyakshoot.dailyupload.data.local.entity.UploadStatus

@Composable
fun DateDivider(
    date: String,
    status: UploadStatus,
    successDate: Long,
    error: String = "",
    onClickToReload: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.95f), thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(date)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                when (status) {
                    UploadStatus.PENDING -> {
                        Text(
                            "Ожидает синхронизации",
                            fontSize = 10.sp
                        )
                        Icon(
                            painterResource(R.drawable.baseline_cloud_queue_24),
                            contentDescription = "waiting synchronize"
                        )
                    }

                    UploadStatus.UPLOADING -> {
                        Text(
                            "Синхронизация...",
                            fontSize = 10.sp
                        )
                        Icon(
                            painterResource(R.drawable.outline_cloud_sync_24),
                            contentDescription = "synchronizing"
                        )
                    }

                    UploadStatus.SUCCESS -> {
                        Text(
                            "Синхронизировано",
                            fontSize = 10.sp
                        )
                        Icon( // todo clickable show date
                            painterResource(R.drawable.outline_task_24),
                            contentDescription = "synchronized"
                        )
                    }

                    UploadStatus.FAILED -> {
                        Text(
                            "Ошибка синхронизации",
                            fontSize = 10.sp
                        )
                        Icon( // todo clickable show error
                            painterResource(R.drawable.outline_cancel_24),
                            contentDescription = "failed synchronize"
                        )
                        IconButton(
                            onClick = { onClickToReload() }
                        ) {
                            Icon(
                                painterResource(R.drawable.round_cached_24),
                                contentDescription = "reload"
                            )
                        }
                    }
                }
            }
        }
    }

}