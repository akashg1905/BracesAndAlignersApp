package com.example.bracesaligner;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BracesApp_MembersInjector implements MembersInjector<BracesApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public BracesApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<BracesApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new BracesApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(BracesApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.example.bracesaligner.BracesApp.workerFactory")
  public static void injectWorkerFactory(BracesApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
