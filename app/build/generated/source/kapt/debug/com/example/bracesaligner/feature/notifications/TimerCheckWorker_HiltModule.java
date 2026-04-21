package com.example.bracesaligner.feature.notifications;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = TimerCheckWorker.class
)
public interface TimerCheckWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.example.bracesaligner.feature.notifications.TimerCheckWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(TimerCheckWorker_AssistedFactory factory);
}
