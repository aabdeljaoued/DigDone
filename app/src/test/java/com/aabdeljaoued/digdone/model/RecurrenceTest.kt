package com.aabdeljaoued.digdone.model

import org.junit.Assert.assertEquals
import org.junit.Test

class RecurrenceTest {
    @Test
    fun hourly_advancesByOneHour() {
        assertEquals(3_600_000L, Recurrence.HOURLY.nextDue(0L))
    }

    @Test
    fun weekly_advancesBySevenDays() {
        assertEquals(604_800_000L, Recurrence.WEEKLY.nextDue(0L))
    }
}
