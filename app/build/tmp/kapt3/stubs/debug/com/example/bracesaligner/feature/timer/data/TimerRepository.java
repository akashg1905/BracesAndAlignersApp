package com.example.bracesaligner.feature.timer.data;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u0010\u001a\u00020\fH\u0082@\u00a2\u0006\u0002\u0010\u0011J\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013J\u0012\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00160\u0013J\u000e\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u001a\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u001b\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u0011R\u000e\u0010\u000b\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/bracesaligner/feature/timer/data/TimerRepository;", "", "timerDao", "Lcom/example/bracesaligner/core/database/dao/NonWearTimerDao;", "planDao", "Lcom/example/bracesaligner/core/database/dao/AlignerPlanDao;", "timerApi", "Lcom/example/bracesaligner/core/network/api/TimerApi;", "sessionStore", "Lcom/example/bracesaligner/core/preferences/SessionStore;", "(Lcom/example/bracesaligner/core/database/dao/NonWearTimerDao;Lcom/example/bracesaligner/core/database/dao/AlignerPlanDao;Lcom/example/bracesaligner/core/network/api/TimerApi;Lcom/example/bracesaligner/core/preferences/SessionStore;)V", "limitMinutes", "", "thresholdEvaluator", "Lcom/example/bracesaligner/feature/timer/domain/TimerThresholdEvaluator;", "warningMinutes", "getCurrentAlignerNumber", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeTimerState", "Lkotlinx/coroutines/flow/Flow;", "Lcom/example/bracesaligner/core/common/TimerState;", "observeWeeklySummary", "", "Lcom/example/bracesaligner/core/database/entity/DailyNonWearSummaryEntity;", "startTimer", "", "stopTimer", "syncPendingSessions", "app_debug"})
public final class TimerRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.database.dao.NonWearTimerDao timerDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.database.dao.AlignerPlanDao planDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.network.api.TimerApi timerApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.preferences.SessionStore sessionStore = null;
    private final int warningMinutes = 90;
    private final int limitMinutes = 120;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.timer.domain.TimerThresholdEvaluator thresholdEvaluator = null;
    
    @javax.inject.Inject()
    public TimerRepository(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.dao.NonWearTimerDao timerDao, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.database.dao.AlignerPlanDao planDao, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.network.api.TimerApi timerApi, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.preferences.SessionStore sessionStore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.example.bracesaligner.core.common.TimerState> observeTimerState() {
        return null;
    }
    
    private final java.lang.Object getCurrentAlignerNumber(kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object startTimer(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object stopTimer(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncPendingSessions(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity>> observeWeeklySummary() {
        return null;
    }
}