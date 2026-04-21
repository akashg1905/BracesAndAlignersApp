package com.example.bracesaligner.feature.notifications;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.example.bracesaligner.feature.timer.data.TimerRepository;
import dagger.internal.DaggerGenerated;
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
public final class TimerCheckWorker_Factory {
  private final Provider<TimerRepository> timerRepositoryProvider;

  public TimerCheckWorker_Factory(Provider<TimerRepository> timerRepositoryProvider) {
    this.timerRepositoryProvider = timerRepositoryProvider;
  }

  public TimerCheckWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, timerRepositoryProvider.get());
  }

  public static TimerCheckWorker_Factory create(Provider<TimerRepository> timerRepositoryProvider) {
    return new TimerCheckWorker_Factory(timerRepositoryProvider);
  }

  public static TimerCheckWorker newInstance(Context appContext, WorkerParameters workerParams,
      TimerRepository timerRepository) {
    return new TimerCheckWorker(appContext, workerParams, timerRepository);
  }
}
