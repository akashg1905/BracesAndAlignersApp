package com.example.bracesaligner.feature.dashboard.presentation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ*\u0010\u0018\u001a\u00020\u000f2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0006\u0010!\u001a\u00020\"J\u0006\u0010#\u001a\u00020\"J\u0006\u0010$\u001a\u00020\"J\u0006\u0010%\u001a\u00020\"R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011X\u0082\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006&"}, d2 = {"Lcom/example/bracesaligner/feature/dashboard/presentation/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "planRepository", "Lcom/example/bracesaligner/feature/plan/data/PlanRepository;", "timerRepository", "Lcom/example/bracesaligner/feature/timer/data/TimerRepository;", "scheduleGenerator", "Lcom/example/bracesaligner/feature/plan/domain/ScheduleGenerator;", "authRepository", "Lcom/example/bracesaligner/feature/auth/data/AuthRepository;", "sessionStore", "Lcom/example/bracesaligner/core/preferences/SessionStore;", "(Lcom/example/bracesaligner/feature/plan/data/PlanRepository;Lcom/example/bracesaligner/feature/timer/data/TimerRepository;Lcom/example/bracesaligner/feature/plan/domain/ScheduleGenerator;Lcom/example/bracesaligner/feature/auth/data/AuthRepository;Lcom/example/bracesaligner/core/preferences/SessionStore;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/bracesaligner/feature/dashboard/presentation/DashboardUiState;", "dashboardFlow", "Lkotlinx/coroutines/flow/Flow;", "getDashboardFlow$annotations", "()V", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "buildDashboardState", "plan", "Lcom/example/bracesaligner/core/common/AlignerPlan;", "timer", "Lcom/example/bracesaligner/core/common/TimerState;", "isLoggedIn", "", "avgWear", "", "logout", "Lkotlinx/coroutines/Job;", "refresh", "startTimer", "stopTimer", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.plan.data.PlanRepository planRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.timer.data.TimerRepository timerRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.plan.domain.ScheduleGenerator scheduleGenerator = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.auth.data.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.preferences.SessionStore sessionStore = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.bracesaligner.feature.dashboard.presentation.DashboardUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.bracesaligner.feature.dashboard.presentation.DashboardUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.example.bracesaligner.feature.dashboard.presentation.DashboardUiState> dashboardFlow = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.plan.data.PlanRepository planRepository, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.timer.data.TimerRepository timerRepository, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.plan.domain.ScheduleGenerator scheduleGenerator, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.auth.data.AuthRepository authRepository, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.preferences.SessionStore sessionStore) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.bracesaligner.feature.dashboard.presentation.DashboardUiState> getUiState() {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @java.lang.Deprecated()
    private static void getDashboardFlow$annotations() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job startTimer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job stopTimer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job logout() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job refresh() {
        return null;
    }
    
    private final com.example.bracesaligner.feature.dashboard.presentation.DashboardUiState buildDashboardState(com.example.bracesaligner.core.common.AlignerPlan plan, com.example.bracesaligner.core.common.TimerState timer, boolean isLoggedIn, double avgWear) {
        return null;
    }
}