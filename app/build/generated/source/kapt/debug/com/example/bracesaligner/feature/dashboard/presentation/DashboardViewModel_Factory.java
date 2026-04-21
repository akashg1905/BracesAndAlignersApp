package com.example.bracesaligner.feature.dashboard.presentation;

import com.example.bracesaligner.core.preferences.SessionStore;
import com.example.bracesaligner.feature.auth.data.AuthRepository;
import com.example.bracesaligner.feature.plan.data.PlanRepository;
import com.example.bracesaligner.feature.plan.domain.ScheduleGenerator;
import com.example.bracesaligner.feature.timer.data.TimerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<PlanRepository> planRepositoryProvider;

  private final Provider<TimerRepository> timerRepositoryProvider;

  private final Provider<ScheduleGenerator> scheduleGeneratorProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SessionStore> sessionStoreProvider;

  public DashboardViewModel_Factory(Provider<PlanRepository> planRepositoryProvider,
      Provider<TimerRepository> timerRepositoryProvider,
      Provider<ScheduleGenerator> scheduleGeneratorProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SessionStore> sessionStoreProvider) {
    this.planRepositoryProvider = planRepositoryProvider;
    this.timerRepositoryProvider = timerRepositoryProvider;
    this.scheduleGeneratorProvider = scheduleGeneratorProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.sessionStoreProvider = sessionStoreProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(planRepositoryProvider.get(), timerRepositoryProvider.get(), scheduleGeneratorProvider.get(), authRepositoryProvider.get(), sessionStoreProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<PlanRepository> planRepositoryProvider,
      Provider<TimerRepository> timerRepositoryProvider,
      Provider<ScheduleGenerator> scheduleGeneratorProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<SessionStore> sessionStoreProvider) {
    return new DashboardViewModel_Factory(planRepositoryProvider, timerRepositoryProvider, scheduleGeneratorProvider, authRepositoryProvider, sessionStoreProvider);
  }

  public static DashboardViewModel newInstance(PlanRepository planRepository,
      TimerRepository timerRepository, ScheduleGenerator scheduleGenerator,
      AuthRepository authRepository, SessionStore sessionStore) {
    return new DashboardViewModel(planRepository, timerRepository, scheduleGenerator, authRepository, sessionStore);
  }
}
