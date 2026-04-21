package com.example.bracesaligner.di;

import com.example.bracesaligner.core.database.AppDatabase;
import com.example.bracesaligner.core.database.dao.AlignerPlanDao;
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
public final class DatabaseModule_ProvideAlignerPlanDaoFactory implements Factory<AlignerPlanDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideAlignerPlanDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AlignerPlanDao get() {
    return provideAlignerPlanDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAlignerPlanDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideAlignerPlanDaoFactory(dbProvider);
  }

  public static AlignerPlanDao provideAlignerPlanDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAlignerPlanDao(db));
  }
}
