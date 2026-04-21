package com.example.bracesaligner.feature.timer.presentation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\t\u00a8\u0006\u0011"}, d2 = {"Lcom/example/bracesaligner/feature/timer/presentation/TimerViewModel;", "Landroidx/lifecycle/ViewModel;", "timerRepository", "Lcom/example/bracesaligner/feature/timer/data/TimerRepository;", "(Lcom/example/bracesaligner/feature/timer/data/TimerRepository;)V", "timerState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/example/bracesaligner/core/common/TimerState;", "getTimerState", "()Lkotlinx/coroutines/flow/StateFlow;", "weeklySummary", "", "Lcom/example/bracesaligner/core/database/entity/DailyNonWearSummaryEntity;", "getWeeklySummary", "startTimer", "Lkotlinx/coroutines/Job;", "stopTimer", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TimerViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.timer.data.TimerRepository timerRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.bracesaligner.core.common.TimerState> timerState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity>> weeklySummary = null;
    
    @javax.inject.Inject()
    public TimerViewModel(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.timer.data.TimerRepository timerRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.bracesaligner.core.common.TimerState> getTimerState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity>> getWeeklySummary() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job startTimer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job stopTimer() {
        return null;
    }
}