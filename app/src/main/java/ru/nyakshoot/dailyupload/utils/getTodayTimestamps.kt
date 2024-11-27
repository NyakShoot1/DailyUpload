package ru.nyakshoot.dailyupload.utils

import java.util.Calendar

fun getTodayTimestamps(): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    // Установить начало дня
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startOfDay = calendar.timeInMillis

    // Установить конец дня
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endOfDay = calendar.timeInMillis

    return startOfDay to endOfDay
}
