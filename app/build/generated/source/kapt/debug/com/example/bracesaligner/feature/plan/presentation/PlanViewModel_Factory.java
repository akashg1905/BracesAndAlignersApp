package com.example.bracesaligner.feature.plan.presentation;

import com.example.bracesaligner.feature.plan.data.PlanRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PlanViewModel_Factory implements Factory<PlanViewModel> {
  private final Provider<PlanRepository> repositoryProvider;

  public PlanViewModel_Factory(Provider<PlanRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PlanViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static PlanViewModel_Factory create(Provider<PlanRepository> repositoryProvider) {
    return new PlanViewModel_Factory(repositoryProvider);
  }

  public static PlanViewModel newInstance(PlanRepository repository) {
    return new PlanViewModel(repository);
  }
}
