package com.example.bracesaligner.feature.plan.domain

import com.example.bracesaligner.core.common.AlignerScheduleItem
import javax.inject.Inject

class ScheduleGenerator @Inject constructor() {
    fun generate(alignerCount: Int, daysPerAligner: Int, startDateEpochDay: Long): List<AlignerScheduleItem> {
        return (1..alignerCount).map { index ->
            val start = startDateEpochDay + ((index - 1) * daysPerAligner)
            val end = start + daysPerAligner - 1
            AlignerScheduleItem(index, start, end)
        }
    }
}
