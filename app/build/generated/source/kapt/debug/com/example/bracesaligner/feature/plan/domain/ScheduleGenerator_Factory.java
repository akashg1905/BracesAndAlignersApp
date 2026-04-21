package com.example.bracesaligner.feature.plan.domain;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ScheduleGenerator_Factory implements Factory<ScheduleGenerator> {
  @Override
  public ScheduleGenerator get() {
    return newInstance();
  }

  public static ScheduleGenerator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ScheduleGenerator newInstance() {
    return new ScheduleGenerator();
  }

  private static final class InstanceHolder {
    private static final ScheduleGenerator_Factory INSTANCE = new ScheduleGenerator_Factory();
  }
}
