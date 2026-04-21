package com.example.bracesaligner.di;

import com.example.bracesaligner.core.database.AppDatabase;
import com.example.bracesaligner.core.database.dao.AuthSessionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAuthSessionDaoFactory implements Factory<AuthSessionDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideAuthSessionDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AuthSessionDao get() {
    return provideAuthSessionDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAuthSessionDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideAuthSessionDaoFactory(dbProvider);
  }

  public static AuthSessionDao provideAuthSessionDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAuthSessionDao(db));
  }
}
