package com.example.bracesaligner.feature.timer.data;

import com.example.bracesaligner.core.database.dao.AlignerPlanDao;
import com.example.bracesaligner.core.database.dao.NonWearTimerDao;
import com.example.bracesaligner.core.network.api.TimerApi;
import com.example.bracesaligner.core.preferences.SessionStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TimerRepository_Factory implements Factory<TimerRepository> {
  private final Provider<NonWearTimerDao> timerDaoProvider;

  private final Provider<AlignerPlanDao> planDaoProvider;

  private final Provider<TimerApi> timerApiProvider;

  private final Provider<SessionStore> sessionStoreProvider;

  public TimerRepository_Factory(Provider<NonWearTimerDao> timerDaoProvider,
      Provider<AlignerPlanDao> planDaoProvider, Provider<TimerApi> timerApiProvider,
      Provider<SessionStore> sessionStoreProvider) {
    this.timerDaoProvider = timerDaoProvider;
    this.planDaoProvider = planDaoProvider;
    this.timerApiProvider = timerApiProvider;
    this.sessionStoreProvider = sessionStoreProvider;
  }

  @Override
  public TimerRepository get() {
    return newInstance(timerDaoProvider.get(), planDaoProvider.get(), timerApiProvider.get(), sessionStoreProvider.get());
  }

  public static TimerRepository_Factory create(Provider<NonWearTimerDao> timerDaoProvider,
      Provider<AlignerPlanDao> planDaoProvider, Provider<TimerApi> timerApiProvider,
      Provider<SessionStore> sessionStoreProvider) {
    return new TimerRepository_Factory(timerDaoProvider, planDaoProvider, timerApiProvider, sessionStoreProvider);
  }

  public static TimerRepository newInstance(NonWearTimerDao timerDao, AlignerPlanDao planDao,
      TimerApi timerApi, SessionStore sessionStore) {
    return new TimerRepository(timerDao, planDao, timerApi, sessionStore);
  }
}
