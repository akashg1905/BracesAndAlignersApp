package com.example.bracesaligner.feature.plan.presentation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0013"}, d2 = {"Lcom/example/bracesaligner/feature/plan/presentation/PlanViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/bracesaligner/feature/plan/data/PlanRepository;", "(Lcom/example/bracesaligner/feature/plan/data/PlanRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/bracesaligner/feature/plan/presentation/PlanUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "createPlan", "", "fetchSchedule", "updateAlignerCount", "value", "", "updateDaysPerAligner", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PlanViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.feature.plan.data.PlanRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.bracesaligner.feature.plan.presentation.PlanUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.bracesaligner.feature.plan.presentation.PlanUiState> uiState = null;
    
    @javax.inject.Inject()
    public PlanViewModel(@org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.feature.plan.data.PlanRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.bracesaligner.feature.plan.presentation.PlanUiState> getUiState() {
        return null;
    }
    
    public final void updateAlignerCount(int value) {
    }
    
    public final void updateDaysPerAligner(int value) {
    }
    
    public final void fetchSchedule() {
    }
    
    public final void createPlan() {
    }
}