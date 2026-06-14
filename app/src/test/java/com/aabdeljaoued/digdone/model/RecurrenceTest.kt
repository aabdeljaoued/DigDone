package com.aabdeljaoued.digdone.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId

class RecurrenceTest {
    @Test
    fun hourly_advancesByOneHour() {
        assertEquals(3_600_000L, Recurrence.HOURLY.nextDue(0L))
    }

    @Test
    fun weekly_advancesBySevenDays() {
        assertEquals(604_800_000L, Recurrence.WEEKLY.nextDue(0L))
    }

    @Test
    fun monthly_advancesByCalendarMonth() {
        val zone = ZoneId.of("UTC")
        val start = LocalDateTime.of(2025, 1, 31, 9, 0).atZone(zone).toInstant().toEpochMilli()
        val expected = LocalDateTime.of(2025, 2, 28, 9, 0).atZone(zone).toInstant().toEpochMilli()

        assertEquals(expected, Recurrence.MONTHLY.nextDue(start, zone))
    }

    @Test
    fun yearly_advancesByCalendarYear() {
        val zone = ZoneId.of("UTC")
        val start = LocalDateTime.of(2024, 2, 29, 9, 0).atZone(zone).toInstant().toEpochMilli()
        val expected = LocalDateTime.of(2025, 2, 28, 9, 0).atZone(zone).toInstant().toEpochMilli()

        assertEquals(expected, Recurrence.YEARLY.nextDue(start, zone))
    }
}
