package com.example.bracesaligner.feature.plan.data

import com.example.bracesaligner.core.common.AlignerPlan
import com.example.bracesaligner.core.common.AlignerScheduleItem
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.core.database.dao.AlignerPlanDao
import com.example.bracesaligner.core.database.entity.AlignerPlanEntity
import com.example.bracesaligner.core.database.entity.AlignerScheduleItemEntity
import com.example.bracesaligner.core.network.api.AlignerPlanApi
import com.example.bracesaligner.core.network.dto.AlignerScheduleResponse
import com.example.bracesaligner.core.network.dto.CreatePlanRequest
import com.example.bracesaligner.feature.plan.domain.ScheduleGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepository @Inject constructor(
    private val planApi: AlignerPlanApi,
    private val planDao: AlignerPlanDao,
    private val scheduleGenerator: ScheduleGenerator
) {
    fun observePlan(): Flow<AlignerPlan?> = planDao.observePlan().map { entity ->
        entity?.let {
            AlignerPlan(
                planId = it.planId,
                alignerCount = it.alignerCount,
                daysPerAligner = it.daysPerAligner,
                startDateEpochDay = it.startDateEpochDay
            )
        }
    }

    suspend fun createPlan(alignerCount: Int, daysPerAligner: Int, startEpochDay: Long = TimeUtils.todayEpochDay()) {
        val response = planApi.createPlan(
            CreatePlanRequest(
                alignerCount = alignerCount,
                daysPerAligner = daysPerAligner,
                startDateEpochDay = startEpochDay
            )
        )
        val entity = AlignerPlanEntity(
            planId = response.planId,
            userId = "me",
            alignerCount = response.alignerCount,
            daysPerAligner = response.daysPerAligner,
            startDateEpochDay = response.startDateEpochDay,
            createdAtEpochMillis = TimeUtils.nowMillis()
        )
        planDao.upsertPlan(entity)
        val schedule = scheduleGenerator.generate(
            alignerCount = entity.alignerCount,
            daysPerAligner = entity.daysPerAligner,
            startDateEpochDay = entity.startDateEpochDay
        ).map {
            AlignerScheduleItemEntity(
                id = UUID.randomUUID().toString(),
                planId = entity.planId,
                alignerNumber = it.alignerNumber,
                startEpochDay = it.startEpochDay,
                endEpochDay = it.endEpochDay
            )
        }
        planDao.clearSchedule(entity.planId)
        planDao.insertSchedule(schedule)
    }

    suspend fun syncActivePlan() {
        try {
            val response = planApi.getActivePlan()
            if (response.isSuccessful) {
                val body = response.body() ?: return
                // If API returns a plan, save it locally and generate schedule
                val entity = AlignerPlanEntity(
                    planId = body.planId,
                    userId = "me",
                    alignerCount = body.alignerCount,
                    daysPerAligner = body.daysPerAligner,
                    startDateEpochDay = body.startDateEpochDay,
                    createdAtEpochMillis = TimeUtils.nowMillis()
                )
                planDao.upsertPlan(entity)
                
                val schedule = scheduleGenerator.generate(
                    alignerCount = entity.alignerCount,
                    daysPerAligner = entity.daysPerAligner,
                    startDateEpochDay = entity.startDateEpochDay
                ).map {
                    AlignerScheduleItemEntity(
                        id = UUID.randomUUID().toString(),
                        planId = entity.planId,
                        alignerNumber = it.alignerNumber,
                        startEpochDay = it.startEpochDay,
                        endEpochDay = it.endEpochDay
                    )
                }
                planDao.clearSchedule(entity.planId)
                planDao.insertSchedule(schedule)
            }
        } catch (e: Exception) {
            // Plan might not exist on backend, which is fine
            e.printStackTrace()
        }
    }

    fun observeSchedule(planId: String): Flow<List<AlignerScheduleItem>> {
        return planDao.observeSchedule(planId).map { list ->
            list.map {
                AlignerScheduleItem(
                    alignerNumber = it.alignerNumber,
                    startEpochDay = it.startEpochDay,
                    endEpochDay = it.endEpochDay
                )
            }
        }
    }

    suspend fun getRemoteSchedule(): List<AlignerScheduleItem> {
        val response = planApi.getSchedule()
        if (response.isSuccessful) {
            return response.body()?.schedule?.map {
                AlignerScheduleItem(
                    alignerNumber = it.alignerNumber,
                    startEpochDay = it.startDateEpochDay,
                    endEpochDay = it.endDateEpochDay,
                    isCurrent = it.isCurrent,
                    startDate = it.startDate,
                    endDate = it.endDate
                )
            } ?: emptyList()
        } else {
            throw Exception("Failed to fetch schedule: ${response.message()}")
        }
    }

}
