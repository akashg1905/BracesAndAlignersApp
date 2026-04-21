package com.example.bracesaligner.feature.plan.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ScheduleGeneratorTest {
    private val generator = ScheduleGenerator()

    @Test
    fun `generates contiguous schedule with expected ranges`() {
        val schedule = generator.generate(
            alignerCount = 3,
            daysPerAligner = 7,
            startDateEpochDay = 100L
        )

        assertEquals(3, schedule.size)
        assertEquals(1, schedule[0].alignerNumber)
        assertEquals(100L, schedule[0].startEpochDay)
        assertEquals(106L, schedule[0].endEpochDay)
        assertEquals(107L, schedule[1].startEpochDay)
        assertEquals(113L, schedule[1].endEpochDay)
        assertEquals(114L, schedule[2].startEpochDay)
        assertEquals(120L, schedule[2].endEpochDay)
    }
}
