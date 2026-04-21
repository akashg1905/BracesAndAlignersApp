package com.example.bracesaligner.di;

import com.example.bracesaligner.core.database.AppDatabase;
import com.example.bracesaligner.core.preferences.SessionStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;

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
public final class NetworkModule_ProvideAuthInterceptorFactory implements Factory<Interceptor> {
  private final Provider<SessionStore> sessionStoreProvider;

  private final Provider<AppDatabase> databaseProvider;

  public NetworkModule_ProvideAuthInterceptorFactory(Provider<SessionStore> sessionStoreProvider,
      Provider<AppDatabase> databaseProvider) {
    this.sessionStoreProvider = sessionStoreProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public Interceptor get() {
    return provideAuthInterceptor(sessionStoreProvider.get(), databaseProvider.get());
  }

  public static NetworkModule_ProvideAuthInterceptorFactory create(
      Provider<SessionStore> sessionStoreProvider, Provider<AppDatabase> databaseProvider) {
    return new NetworkModule_ProvideAuthInterceptorFactory(sessionStoreProvider, databaseProvider);
  }

  public static Interceptor provideAuthInterceptor(SessionStore sessionStore,
      AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideAuthInterceptor(sessionStore, database));
  }
}
