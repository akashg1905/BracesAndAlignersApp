package com.example.bracesaligner.feature.timer.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimerThresholdEvaluatorTest {
    private val evaluator = TimerThresholdEvaluator()

    @Test
    fun `warning true and limit false before 2h`() {
        val result = evaluator.evaluate(totalMinutes = 95)
        assertTrue(result.warningReached)
        assertFalse(result.limitExceeded)
    }

    @Test
    fun `limit true at or after 2h`() {
        val result = evaluator.evaluate(totalMinutes = 120)
        assertTrue(result.warningReached)
        assertTrue(result.limitExceeded)
    }
}
