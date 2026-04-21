package com.example.bracesaligner.feature.plan.data;

import com.example.bracesaligner.core.database.dao.AlignerPlanDao;
import com.example.bracesaligner.core.network.api.AlignerPlanApi;
import com.example.bracesaligner.feature.plan.domain.ScheduleGenerator;
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
public final class PlanRepository_Factory implements Factory<PlanRepository> {
  private final Provider<AlignerPlanApi> planApiProvider;

  private final Provider<AlignerPlanDao> planDaoProvider;

  private final Provider<ScheduleGenerator> scheduleGeneratorProvider;

  public PlanRepository_Factory(Provider<AlignerPlanApi> planApiProvider,
      Provider<AlignerPlanDao> planDaoProvider,
      Provider<ScheduleGenerator> scheduleGeneratorProvider) {
    this.planApiProvider = planApiProvider;
    this.planDaoProvider = planDaoProvider;
    this.scheduleGeneratorProvider = scheduleGeneratorProvider;
  }

  @Override
  public PlanRepository get() {
    return newInstance(planApiProvider.get(), planDaoProvider.get(), scheduleGeneratorProvider.get());
  }

  public static PlanRepository_Factory create(Provider<AlignerPlanApi> planApiProvider,
      Provider<AlignerPlanDao> planDaoProvider,
      Provider<ScheduleGenerator> scheduleGeneratorProvider) {
    return new PlanRepository_Factory(planApiProvider, planDaoProvider, scheduleGeneratorProvider);
  }

  public static PlanRepository newInstance(AlignerPlanApi planApi, AlignerPlanDao planDao,
      ScheduleGenerator scheduleGenerator) {
    return new PlanRepository(planApi, planDao, scheduleGenerator);
  }
}
