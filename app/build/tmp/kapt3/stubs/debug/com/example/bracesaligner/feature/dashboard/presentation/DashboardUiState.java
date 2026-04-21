package com.example.bracesaligner.feature.dashboard.presentation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b.\b\u0086\b\u0018\u00002\u00020\u0001B\u00b3\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\u0002\u0010\u0018J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0005H\u00c6\u0003J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\u0012H\u00c6\u0003J\t\u00103\u001a\u00020\u0012H\u00c6\u0003J\t\u00104\u001a\u00020\u0012H\u00c6\u0003J\t\u00105\u001a\u00020\u0012H\u00c6\u0003J\t\u00106\u001a\u00020\u0017H\u00c6\u0003J\t\u00107\u001a\u00020\u0005H\u00c6\u0003J\t\u00108\u001a\u00020\u0005H\u00c6\u0003J\t\u00109\u001a\u00020\u0005H\u00c6\u0003J\t\u0010:\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010;\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010!J\t\u0010<\u001a\u00020\u000bH\u00c6\u0003J\t\u0010=\u001a\u00020\u000bH\u00c6\u0003J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\u00bc\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u00122\b\b\u0002\u0010\u0015\u001a\u00020\u00122\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u00c6\u0001\u00a2\u0006\u0002\u0010@J\u0013\u0010A\u001a\u00020\u00122\b\u0010B\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010C\u001a\u00020\u0005H\u00d6\u0001J\t\u0010D\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u0014\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u001fR\u0011\u0010\u0015\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u001fR\u0011\u0010\u0011\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u001fR\u0015\u0010\t\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\"\u001a\u0004\b \u0010!R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010$R\u0011\u0010\u0013\u001a\u00020\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001fR\u0011\u0010\u0010\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010$R\u0011\u0010\u000f\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0011\u0010\u0016\u001a\u00020\u0017\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010$\u00a8\u0006E"}, d2 = {"Lcom/example/bracesaligner/feature/dashboard/presentation/DashboardUiState;", "", "userName", "", "totalTransformationProgress", "", "currentAlignerNumber", "totalAligners", "daysLeftInCurrentAligner", "nextAlignerNumber", "currentAlignerProgress", "", "averageDailyHours", "nonWearTimeTodayFormatted", "nextCheckUpDate", "streakDays", "proTip", "isScanRequired", "", "planAvailable", "isLoggedIn", "isRefreshing", "timerState", "Lcom/example/bracesaligner/core/common/TimerState;", "(Ljava/lang/String;IIIILjava/lang/Integer;FFLjava/lang/String;Ljava/lang/String;ILjava/lang/String;ZZZZLcom/example/bracesaligner/core/common/TimerState;)V", "getAverageDailyHours", "()F", "getCurrentAlignerNumber", "()I", "getCurrentAlignerProgress", "getDaysLeftInCurrentAligner", "()Z", "getNextAlignerNumber", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getNextCheckUpDate", "()Ljava/lang/String;", "getNonWearTimeTodayFormatted", "getPlanAvailable", "getProTip", "getStreakDays", "getTimerState", "()Lcom/example/bracesaligner/core/common/TimerState;", "getTotalAligners", "getTotalTransformationProgress", "getUserName", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;IIIILjava/lang/Integer;FFLjava/lang/String;Ljava/lang/String;ILjava/lang/String;ZZZZLcom/example/bracesaligner/core/common/TimerState;)Lcom/example/bracesaligner/feature/dashboard/presentation/DashboardUiState;", "equals", "other", "hashCode", "toString", "app_debug"})
public final class DashboardUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String userName = null;
    private final int totalTransformationProgress = 0;
    private final int currentAlignerNumber = 0;
    private final int totalAligners = 0;
    private final int daysLeftInCurrentAligner = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer nextAlignerNumber = null;
    private final float currentAlignerProgress = 0.0F;
    private final float averageDailyHours = 0.0F;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String nonWearTimeTodayFormatted = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String nextCheckUpDate = null;
    private final int streakDays = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String proTip = null;
    private final boolean isScanRequired = false;
    private final boolean planAvailable = false;
    private final boolean isLoggedIn = false;
    private final boolean isRefreshing = false;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bracesaligner.core.common.TimerState timerState = null;
    
    public DashboardUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String userName, int totalTransformationProgress, int currentAlignerNumber, int totalAligners, int daysLeftInCurrentAligner, @org.jetbrains.annotations.Nullable()
    java.lang.Integer nextAlignerNumber, float currentAlignerProgress, float averageDailyHours, @org.jetbrains.annotations.NotNull()
    java.lang.String nonWearTimeTodayFormatted, @org.jetbrains.annotations.Nullable()
    java.lang.String nextCheckUpDate, int streakDays, @org.jetbrains.annotations.NotNull()
    java.lang.String proTip, boolean isScanRequired, boolean planAvailable, boolean isLoggedIn, boolean isRefreshing, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.common.TimerState timerState) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUserName() {
        return null;
    }
    
    public final int getTotalTransformationProgress() {
        return 0;
    }
    
    public final int getCurrentAlignerNumber() {
        return 0;
    }
    
    public final int getTotalAligners() {
        return 0;
    }
    
    public final int getDaysLeftInCurrentAligner() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getNextAlignerNumber() {
        return null;
    }
    
    public final float getCurrentAlignerProgress() {
        return 0.0F;
    }
    
    public final float getAverageDailyHours() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNonWearTimeTodayFormatted() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNextCheckUpDate() {
        return null;
    }
    
    public final int getStreakDays() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProTip() {
        return null;
    }
    
    public final boolean isScanRequired() {
        return false;
    }
    
    public final boolean getPlanAvailable() {
        return false;
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final boolean isRefreshing() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.bracesaligner.core.common.TimerState getTimerState() {
        return null;
    }
    
    public DashboardUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    public final int component11() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final boolean component14() {
        return false;
    }
    
    public final boolean component15() {
        return false;
    }
    
    public final boolean component16() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.bracesaligner.core.common.TimerState component17() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component6() {
        return null;
    }
    
    public final float component7() {
        return 0.0F;
    }
    
    public final float component8() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.bracesaligner.feature.dashboard.presentation.DashboardUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String userName, int totalTransformationProgress, int currentAlignerNumber, int totalAligners, int daysLeftInCurrentAligner, @org.jetbrains.annotations.Nullable()
    java.lang.Integer nextAlignerNumber, float currentAlignerProgress, float averageDailyHours, @org.jetbrains.annotations.NotNull()
    java.lang.String nonWearTimeTodayFormatted, @org.jetbrains.annotations.Nullable()
    java.lang.String nextCheckUpDate, int streakDays, @org.jetbrains.annotations.NotNull()
    java.lang.String proTip, boolean isScanRequired, boolean planAvailable, boolean isLoggedIn, boolean isRefreshing, @org.jetbrains.annotations.NotNull()
    com.example.bracesaligner.core.common.TimerState timerState) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}