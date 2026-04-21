package com.example.bracesaligner.di;

import com.example.bracesaligner.core.database.AppDatabase;
import com.example.bracesaligner.core.database.dao.NonWearTimerDao;
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
public final class DatabaseModule_ProvideNonWearTimerDaoFactory implements Factory<NonWearTimerDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideNonWearTimerDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public NonWearTimerDao get() {
    return provideNonWearTimerDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideNonWearTimerDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideNonWearTimerDaoFactory(dbProvider);
  }

  public static NonWearTimerDao provideNonWearTimerDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNonWearTimerDao(db));
  }
}
