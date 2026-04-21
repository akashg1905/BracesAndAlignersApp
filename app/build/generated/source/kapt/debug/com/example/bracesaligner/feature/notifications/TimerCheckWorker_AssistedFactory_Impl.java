package com.example.bracesaligner.feature.notifications;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class TimerCheckWorker_AssistedFactory_Impl implements TimerCheckWorker_AssistedFactory {
  private final TimerCheckWorker_Factory delegateFactory;

  TimerCheckWorker_AssistedFactory_Impl(TimerCheckWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public TimerCheckWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<TimerCheckWorker_AssistedFactory> create(
      TimerCheckWorker_Factory delegateFactory) {
    return InstanceFactory.create(new TimerCheckWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<TimerCheckWorker_AssistedFactory> createFactoryProvider(
      TimerCheckWorker_Factory delegateFactory) {
    return InstanceFactory.create(new TimerCheckWorker_AssistedFactory_Impl(delegateFactory));
  }
}
