package com.aabdeljaoued.digdone.model

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

@Serializable
data class Memo(
    val id: String,
    val title: String,
    val notes: String,
    val recurrence: Recurrence,
    val firstDueAtMillis: Long,
    val isActive: Boolean = true,
)

@Serializable
enum class Recurrence {
    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
}

fun Recurrence.nextDue(afterMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): Long {
    val dueAt = Instant.ofEpochMilli(afterMillis).atZone(zoneId)
    val nextDueAt = when (this) {
        Recurrence.HOURLY -> dueAt.plusHours(1)
        Recurrence.DAILY -> dueAt.plusDays(1)
        Recurrence.WEEKLY -> dueAt.plusWeeks(1)
        Recurrence.MONTHLY -> dueAt.plusMonths(1)
        Recurrence.YEARLY -> dueAt.plusYears(1)
    }
    return nextDueAt.toInstant().toEpochMilli()
}
