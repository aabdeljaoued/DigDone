package com.aabdeljaoued.digdone.model

import kotlinx.serialization.Serializable

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

fun Recurrence.nextDue(afterMillis: Long): Long = when (this) {
    Recurrence.HOURLY -> afterMillis + 60 * 60 * 1000L
    Recurrence.DAILY -> afterMillis + 24 * 60 * 60 * 1000L
    Recurrence.WEEKLY -> afterMillis + 7 * 24 * 60 * 60 * 1000L
    Recurrence.MONTHLY -> afterMillis + 30L * 24 * 60 * 60 * 1000L
    Recurrence.YEARLY -> afterMillis + 365L * 24 * 60 * 60 * 1000L
}
