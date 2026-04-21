package com.example.bracesaligner.feature.timer.presentation;

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
public final class TimerViewModel_Factory implements Factory<TimerViewModel> {
  private final Provider<TimerRepository> timerRepositoryProvider;

  public TimerViewModel_Factory(Provider<TimerRepository> timerRepositoryProvider) {
    this.timerRepositoryProvider = timerRepositoryProvider;
  }

  @Override
  public TimerViewModel get() {
    return newInstance(timerRepositoryProvider.get());
  }

  public static TimerViewModel_Factory create(Provider<TimerRepository> timerRepositoryProvider) {
    return new TimerViewModel_Factory(timerRepositoryProvider);
  }

  public static TimerViewModel newInstance(TimerRepository timerRepository) {
    return new TimerViewModel(timerRepository);
  }
}
