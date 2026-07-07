package com.smylo.feature.timer.domain

data class ThresholdState(
    val warningReached: Boolean,
    val limitExceeded: Boolean
)

class TimerThresholdEvaluator {
    fun evaluate(totalMinutes: Int, warningMinutes: Int = 90, limitMinutes: Int = 120): ThresholdState {
        return ThresholdState(
            warningReached = totalMinutes >= warningMinutes,
            limitExceeded = totalMinutes >= limitMinutes
        )
    }
}

