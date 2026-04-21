package com.example.bracesaligner.core.network.dto;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0003J7\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0001J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u0007H\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001e"}, d2 = {"Lcom/example/bracesaligner/core/network/dto/AlignerScheduleResponse;", "", "planId", "", "todayEpochDay", "", "currentAlignerNumber", "", "schedule", "", "Lcom/example/bracesaligner/core/network/dto/AlignerScheduleItemDto;", "(Ljava/lang/String;JILjava/util/List;)V", "getCurrentAlignerNumber", "()I", "getPlanId", "()Ljava/lang/String;", "getSchedule", "()Ljava/util/List;", "getTodayEpochDay", "()J", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class AlignerScheduleResponse {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String planId = null;
    private final long todayEpochDay = 0L;
    private final int currentAlignerNumber = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.bracesaligner.core.network.dto.AlignerScheduleItemDto> schedule = null;
    
    public AlignerScheduleResponse(@org.jetbrains.annotations.NotNull()
    java.lang.String planId, long todayEpochDay, int currentAlignerNumber, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.bracesaligner.core.network.dto.AlignerScheduleItemDto> schedule) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPlanId() {
        return null;
    }
    
    public final long getTodayEpochDay() {
        return 0L;
    }
    
    public final int getCurrentAlignerNumber() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.bracesaligner.core.network.dto.AlignerScheduleItemDto> getSchedule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    public final int component3() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.bracesaligner.core.network.dto.AlignerScheduleItemDto> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.bracesaligner.core.network.dto.AlignerScheduleResponse copy(@org.jetbrains.annotations.NotNull()
    java.lang.String planId, long todayEpochDay, int currentAlignerNumber, @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.bracesaligner.core.network.dto.AlignerScheduleItemDto> schedule) {
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