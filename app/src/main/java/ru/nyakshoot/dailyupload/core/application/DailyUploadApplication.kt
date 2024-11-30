package ru.nyakshoot.dailyupload.core.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import ru.nyakshoot.dailyupload.worker.UploadWorker
import ru.nyakshoot.dailyupload.worker.UploadWorker.Companion.WORK_NAME
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class DailyUploadApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupRecurringWork()
    }

    private fun setupRecurringWork() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatRequest = PeriodicWorkRequestBuilder<UploadWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            repeatRequest
        )
    }

    private fun calculateInitialDelay(): Long {
        val now = LocalTime.now()
        val targetTime = LocalTime.of(23, 0)
        return if (now.isBefore(targetTime)) {
            Duration.between(now, targetTime).toMinutes()
        } else {
            Duration.between(now, targetTime.plusHours(24)).toMinutes()
        }
    }


}