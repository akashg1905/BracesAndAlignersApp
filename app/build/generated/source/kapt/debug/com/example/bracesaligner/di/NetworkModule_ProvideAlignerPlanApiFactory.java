package com.example.bracesaligner.di;

import com.example.bracesaligner.core.network.api.AlignerPlanApi;
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
public final class NetworkModule_ProvideAlignerPlanApiFactory implements Factory<AlignerPlanApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideAlignerPlanApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public AlignerPlanApi get() {
    return provideAlignerPlanApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideAlignerPlanApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideAlignerPlanApiFactory(retrofitProvider);
  }

  public static AlignerPlanApi provideAlignerPlanApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideAlignerPlanApi(retrofit));
  }
}
