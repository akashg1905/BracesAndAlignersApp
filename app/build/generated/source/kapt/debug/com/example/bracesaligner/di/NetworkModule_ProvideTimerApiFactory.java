package com.example.bracesaligner.di;

import com.example.bracesaligner.core.network.api.TimerApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideTimerApiFactory implements Factory<TimerApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideTimerApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public TimerApi get() {
    return provideTimerApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideTimerApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideTimerApiFactory(retrofitProvider);
  }

  public static TimerApi provideTimerApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideTimerApi(retrofit));
  }
}
