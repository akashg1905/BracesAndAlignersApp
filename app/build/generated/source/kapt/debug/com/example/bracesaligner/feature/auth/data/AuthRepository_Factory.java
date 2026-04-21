package com.example.bracesaligner.feature.auth.data;

import com.example.bracesaligner.core.database.AppDatabase;
import com.example.bracesaligner.core.database.dao.AuthSessionDao;
import com.example.bracesaligner.core.network.api.AuthApi;
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<AuthApi> authApiProvider;

  private final Provider<AuthSessionDao> authSessionDaoProvider;

  private final Provider<SessionStore> sessionStoreProvider;

  private final Provider<AppDatabase> databaseProvider;

  public AuthRepository_Factory(Provider<AuthApi> authApiProvider,
      Provider<AuthSessionDao> authSessionDaoProvider, Provider<SessionStore> sessionStoreProvider,
      Provider<AppDatabase> databaseProvider) {
    this.authApiProvider = authApiProvider;
    this.authSessionDaoProvider = authSessionDaoProvider;
    this.sessionStoreProvider = sessionStoreProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(authApiProvider.get(), authSessionDaoProvider.get(), sessionStoreProvider.get(), databaseProvider.get());
  }

  public static AuthRepository_Factory create(Provider<AuthApi> authApiProvider,
      Provider<AuthSessionDao> authSessionDaoProvider, Provider<SessionStore> sessionStoreProvider,
      Provider<AppDatabase> databaseProvider) {
    return new AuthRepository_Factory(authApiProvider, authSessionDaoProvider, sessionStoreProvider, databaseProvider);
  }

  public static AuthRepository newInstance(AuthApi authApi, AuthSessionDao authSessionDao,
      SessionStore sessionStore, AppDatabase database) {
    return new AuthRepository(authApi, authSessionDao, sessionStore, database);
  }
}
