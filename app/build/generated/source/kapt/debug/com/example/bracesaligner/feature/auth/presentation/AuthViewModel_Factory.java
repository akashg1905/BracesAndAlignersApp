package com.example.bracesaligner.feature.auth.presentation;

import com.example.bracesaligner.feature.auth.data.AuthRepository;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> repositoryProvider;

  private final Provider<PlanRepository> planRepositoryProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> repositoryProvider,
      Provider<PlanRepository> planRepositoryProvider) {
    this.repositoryProvider = repositoryProvider;
    this.planRepositoryProvider = planRepositoryProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(repositoryProvider.get(), planRepositoryProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> repositoryProvider,
      Provider<PlanRepository> planRepositoryProvider) {
    return new AuthViewModel_Factory(repositoryProvider, planRepositoryProvider);
  }

  public static AuthViewModel newInstance(AuthRepository repository,
      PlanRepository planRepository) {
    return new AuthViewModel(repository, planRepository);
  }
}
