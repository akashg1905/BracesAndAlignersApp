package com.example.bracesaligner.feature.plan.data;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ(\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u000e\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0016J\u001a\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00120\u00162\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/bracesaligner/feature/plan/data/PlanRepository;", "", "planApi", "Lcom/example/bracesaligner/core/network/api/AlignerPlanApi;", "planDao", "Lcom/example/bracesaligner/core/database/dao/AlignerPlanDao;", "scheduleGenerator", "Lcom/example/bracesaligner/feature/plan/domain/ScheduleGenerator;", "(Lcom/example/bracesaligner/core/network/api/AlignerPlanApi;Lcom/example/bracesaligner/core/database/dao/AlignerPlanDao;Lcom/example/bracesaligner/feature/plan/domain/ScheduleGenerator;)V", "createPlan", "", "alignerCount", "", "daysPerAligner", "startEpochDay", "", "(IIJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRemoteSchedule", "", "Lcom/example/bracesaligner/core/common/AlignerScheduleItem;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observePlan", "Lkotlinx/coroutines/flow/Flow;", "Lcom/example/bracesaligner/core/common/AlignerPlan;", "observeSchedule", "planId", "", "syncActivePlan", "app_debug"})
public final class PlanRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.network.api.AlignerPlanApi planApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.database.dao.AlignerPlanDao planDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.plan.domain.ScheduleGenerator scheduleGenerator = null;
    
    @javax.inject.Inject()
    public PlanRepository(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.network.api.AlignerPlanApi planApi, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.dao.AlignerPlanDao planDao, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.plan.domain.ScheduleGenerator scheduleGenerator) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.example.bracesaligner.core.common.AlignerPlan> observePlan() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createPlan(int alignerCount, int daysPerAligner, long startEpochDay, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncActivePlan(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.bracesaligner.core.common.AlignerScheduleItem>> observeSchedule(@org.jetbrains.annotations.NotNull()
    java.lang.String planId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getRemoteSchedule(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.bracesaligner.core.common.AlignerScheduleItem>> $completion) {
        return null;
    }
}