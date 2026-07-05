package com.smylo.feature.plan.data

import com.smylo.core.common.AlignerPlan
import com.smylo.core.common.AlignerScheduleItem
import com.smylo.core.common.TimeUtils
import com.smylo.core.database.dao.AlignerPlanDao
import com.smylo.core.database.entity.AlignerPlanEntity
import com.smylo.core.database.entity.AlignerScheduleItemEntity
import com.smylo.core.network.api.AlignerPlanApi
import com.smylo.core.network.dto.AlignerScheduleResponse
import com.smylo.core.network.dto.CreatePlanRequest
import com.smylo.core.network.dto.UpdateAlignerRequest
import com.smylo.core.network.dto.UpdatePlanScheduleRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanRepository @Inject constructor(
    private val planApi: AlignerPlanApi,
    private val planDao: AlignerPlanDao
) {
    fun observePlan(): Flow<AlignerPlan?> = planDao.observePlan().map { entity ->
        entity?.let {
            AlignerPlan(
                planId = it.planId,
                alignerCount = it.alignerCount,
                daysPerAligner = it.daysPerAligner,
                startDateEpochDay = it.startDateEpochDay,
                planStatus = it.planStatus
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
        // Clear old plan data to ensure the new one is picked up by the UI
        planDao.clearAllPlansAndSchedules()
        
        val entity = AlignerPlanEntity(
            planId = response.planId,
            userId = "me",
            alignerCount = response.alignerCount,
            daysPerAligner = response.daysPerAligner,
            startDateEpochDay = response.startDateEpochDay,
            createdAtEpochMillis = TimeUtils.nowMillis(),
            planStatus = response.planStatus
        )
        planDao.upsertPlan(entity)
        
        // Fetch and save the actual schedule from the server
        try {
            val scheduleResponse = planApi.getSchedule()
            if (scheduleResponse.isSuccessful) {
                val remoteSchedule = scheduleResponse.body()?.schedule?.map {
                    AlignerScheduleItemEntity(
                        id = it.alignerId,
                        planId = entity.planId,
                        alignerNumber = it.alignerNumber,
                        daysForAligner = it.daysForAligner,
                        startEpochDay = it.startDateEpochDay,
                        endEpochDay = it.endDateEpochDay,
                        startDate = it.startDate,
                        endDate = it.endDate,
                        isCurrent = it.isCurrent
                    )
                } ?: emptyList()
                planDao.clearSchedule(entity.planId)
                planDao.insertSchedule(remoteSchedule)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncActivePlan() {
        try {
            val response = planApi.getActivePlan()
            if (response.code() == 404) {
                planDao.clearAllPlansAndSchedules()
                return
            }
            if (!response.isSuccessful) return
            val body = response.body() ?: return
            
            // Clear old plan data before syncing the active one from server
            planDao.clearAllPlansAndSchedules()

            val entity = AlignerPlanEntity(
                planId = body.planId,
                userId = "me",
                alignerCount = body.alignerCount,
                daysPerAligner = body.daysPerAligner,
                startDateEpochDay = body.startDateEpochDay,
                createdAtEpochMillis = TimeUtils.nowMillis(), // Ensure this is fresh
                planStatus = body.planStatus
            )
            planDao.upsertPlan(entity)
            
            // Fetch and save the actual schedule from the server
            val scheduleResponse = planApi.getSchedule()
            if (scheduleResponse.isSuccessful) {
                val remoteSchedule = scheduleResponse.body()?.schedule?.map {
                    AlignerScheduleItemEntity(
                        id = it.alignerId,
                        planId = entity.planId,
                        alignerNumber = it.alignerNumber,
                        daysForAligner = it.daysForAligner,
                        startEpochDay = it.startDateEpochDay,
                        endEpochDay = it.endDateEpochDay,
                        startDate = it.startDate,
                        endDate = it.endDate,
                        isCurrent = it.isCurrent
                    )
                } ?: emptyList()
                planDao.clearSchedule(entity.planId)
                planDao.insertSchedule(remoteSchedule)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun observeSchedule(planId: String): Flow<List<AlignerScheduleItem>> {
        return planDao.observeSchedule(planId).map { list ->
            list.map {
                AlignerScheduleItem(
                    id = it.id,
                    alignerNumber = it.alignerNumber,
                    daysForAligner = it.daysForAligner,
                    startEpochDay = it.startEpochDay,
                    endEpochDay = it.endEpochDay,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    isCurrent = it.isCurrent
                )
            }
        }
    }

    suspend fun getRemoteSchedule(): List<AlignerScheduleItem> {
        val response = planApi.getSchedule()
        if (response.isSuccessful) {
            return response.body()?.schedule?.map {
                AlignerScheduleItem(
                    id = it.alignerId,
                    alignerNumber = it.alignerNumber,
                    daysForAligner = it.daysForAligner,
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

    suspend fun updateSchedule(planId: String?, updates: List<UpdateAlignerRequest>) {
        val response = planApi.updateSchedule(UpdatePlanScheduleRequest(planId, updates))
        if (response.isSuccessful) {
            // After successful update, re-sync the whole schedule to local DB
            // to ensure UI reflects the recalibrated dates.
            syncActivePlan()
        } else {
            throw Exception("Failed to update schedule: ${response.errorBody()?.string()}")
        }
    }

}

