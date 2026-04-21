package com.example.bracesaligner;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.example.bracesaligner.core.database.AppDatabase;
import com.example.bracesaligner.core.database.dao.AlignerPlanDao;
import com.example.bracesaligner.core.database.dao.AuthSessionDao;
import com.example.bracesaligner.core.database.dao.NonWearTimerDao;
import com.example.bracesaligner.core.network.api.AlignerPlanApi;
import com.example.bracesaligner.core.network.api.AuthApi;
import com.example.bracesaligner.core.network.api.TimerApi;
import com.example.bracesaligner.core.preferences.SessionStore;
import com.example.bracesaligner.di.DatabaseModule_ProvideAlignerPlanDaoFactory;
import com.example.bracesaligner.di.DatabaseModule_ProvideAuthSessionDaoFactory;
import com.example.bracesaligner.di.DatabaseModule_ProvideDatabaseFactory;
import com.example.bracesaligner.di.DatabaseModule_ProvideNonWearTimerDaoFactory;
import com.example.bracesaligner.di.NetworkModule_ProvideAlignerPlanApiFactory;
import com.example.bracesaligner.di.NetworkModule_ProvideAuthApiFactory;
import com.example.bracesaligner.di.NetworkModule_ProvideAuthInterceptorFactory;
import com.example.bracesaligner.di.NetworkModule_ProvideOkHttpClientFactory;
import com.example.bracesaligner.di.NetworkModule_ProvideRetrofitFactory;
import com.example.bracesaligner.di.NetworkModule_ProvideTimerApiFactory;
import com.example.bracesaligner.di.PreferencesModule_ProvideDataStoreFactory;
import com.example.bracesaligner.di.PreferencesModule_ProvideSessionStoreFactory;
import com.example.bracesaligner.feature.auth.data.AuthRepository;
import com.example.bracesaligner.feature.auth.presentation.AuthViewModel;
import com.example.bracesaligner.feature.auth.presentation.AuthViewModel_HiltModules;
import com.example.bracesaligner.feature.auth.presentation.SplashViewModel;
import com.example.bracesaligner.feature.auth.presentation.SplashViewModel_HiltModules;
import com.example.bracesaligner.feature.dashboard.presentation.DashboardViewModel;
import com.example.bracesaligner.feature.dashboard.presentation.DashboardViewModel_HiltModules;
import com.example.bracesaligner.feature.notifications.TimerCheckWorker;
import com.example.bracesaligner.feature.notifications.TimerCheckWorker_AssistedFactory;
import com.example.bracesaligner.feature.plan.data.PlanRepository;
import com.example.bracesaligner.feature.plan.domain.ScheduleGenerator;
import com.example.bracesaligner.feature.plan.presentation.PlanViewModel;
import com.example.bracesaligner.feature.plan.presentation.PlanViewModel_HiltModules;
import com.example.bracesaligner.feature.timer.data.TimerRepository;
import com.example.bracesaligner.feature.timer.presentation.TimerViewModel;
import com.example.bracesaligner.feature.timer.presentation.TimerViewModel_HiltModules;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerBracesApp_HiltComponents_SingletonC {
  private DaggerBracesApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public BracesApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements BracesApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements BracesApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements BracesApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements BracesApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements BracesApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements BracesApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements BracesApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public BracesApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends BracesApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends BracesApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends BracesApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends BracesApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(5).put(LazyClassKeyProvider.com_example_bracesaligner_feature_auth_presentation_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_bracesaligner_feature_dashboard_presentation_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_bracesaligner_feature_plan_presentation_PlanViewModel, PlanViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_bracesaligner_feature_auth_presentation_SplashViewModel, SplashViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_example_bracesaligner_feature_timer_presentation_TimerViewModel, TimerViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_bracesaligner_feature_auth_presentation_SplashViewModel = "com.example.bracesaligner.feature.auth.presentation.SplashViewModel";

      static String com_example_bracesaligner_feature_plan_presentation_PlanViewModel = "com.example.bracesaligner.feature.plan.presentation.PlanViewModel";

      static String com_example_bracesaligner_feature_timer_presentation_TimerViewModel = "com.example.bracesaligner.feature.timer.presentation.TimerViewModel";

      static String com_example_bracesaligner_feature_auth_presentation_AuthViewModel = "com.example.bracesaligner.feature.auth.presentation.AuthViewModel";

      static String com_example_bracesaligner_feature_dashboard_presentation_DashboardViewModel = "com.example.bracesaligner.feature.dashboard.presentation.DashboardViewModel";

      @KeepFieldType
      SplashViewModel com_example_bracesaligner_feature_auth_presentation_SplashViewModel2;

      @KeepFieldType
      PlanViewModel com_example_bracesaligner_feature_plan_presentation_PlanViewModel2;

      @KeepFieldType
      TimerViewModel com_example_bracesaligner_feature_timer_presentation_TimerViewModel2;

      @KeepFieldType
      AuthViewModel com_example_bracesaligner_feature_auth_presentation_AuthViewModel2;

      @KeepFieldType
      DashboardViewModel com_example_bracesaligner_feature_dashboard_presentation_DashboardViewModel2;
    }
  }

  private static final class ViewModelCImpl extends BracesApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<PlanViewModel> planViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<TimerViewModel> timerViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.planViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.timerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(5).put(LazyClassKeyProvider.com_example_bracesaligner_feature_auth_presentation_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_example_bracesaligner_feature_dashboard_presentation_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_example_bracesaligner_feature_plan_presentation_PlanViewModel, ((Provider) planViewModelProvider)).put(LazyClassKeyProvider.com_example_bracesaligner_feature_auth_presentation_SplashViewModel, ((Provider) splashViewModelProvider)).put(LazyClassKeyProvider.com_example_bracesaligner_feature_timer_presentation_TimerViewModel, ((Provider) timerViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_example_bracesaligner_feature_auth_presentation_AuthViewModel = "com.example.bracesaligner.feature.auth.presentation.AuthViewModel";

      static String com_example_bracesaligner_feature_timer_presentation_TimerViewModel = "com.example.bracesaligner.feature.timer.presentation.TimerViewModel";

      static String com_example_bracesaligner_feature_auth_presentation_SplashViewModel = "com.example.bracesaligner.feature.auth.presentation.SplashViewModel";

      static String com_example_bracesaligner_feature_dashboard_presentation_DashboardViewModel = "com.example.bracesaligner.feature.dashboard.presentation.DashboardViewModel";

      static String com_example_bracesaligner_feature_plan_presentation_PlanViewModel = "com.example.bracesaligner.feature.plan.presentation.PlanViewModel";

      @KeepFieldType
      AuthViewModel com_example_bracesaligner_feature_auth_presentation_AuthViewModel2;

      @KeepFieldType
      TimerViewModel com_example_bracesaligner_feature_timer_presentation_TimerViewModel2;

      @KeepFieldType
      SplashViewModel com_example_bracesaligner_feature_auth_presentation_SplashViewModel2;

      @KeepFieldType
      DashboardViewModel com_example_bracesaligner_feature_dashboard_presentation_DashboardViewModel2;

      @KeepFieldType
      PlanViewModel com_example_bracesaligner_feature_plan_presentation_PlanViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.bracesaligner.feature.auth.presentation.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.planRepositoryProvider.get());

          case 1: // com.example.bracesaligner.feature.dashboard.presentation.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.planRepositoryProvider.get(), singletonCImpl.timerRepositoryProvider.get(), new ScheduleGenerator(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.provideSessionStoreProvider.get());

          case 2: // com.example.bracesaligner.feature.plan.presentation.PlanViewModel 
          return (T) new PlanViewModel(singletonCImpl.planRepositoryProvider.get());

          case 3: // com.example.bracesaligner.feature.auth.presentation.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.authRepositoryProvider.get());

          case 4: // com.example.bracesaligner.feature.timer.presentation.TimerViewModel 
          return (T) new TimerViewModel(singletonCImpl.timerRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends BracesApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends BracesApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends BracesApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<SessionStore> provideSessionStoreProvider;

    private Provider<Interceptor> provideAuthInterceptorProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<TimerApi> provideTimerApiProvider;

    private Provider<TimerRepository> timerRepositoryProvider;

    private Provider<TimerCheckWorker_AssistedFactory> timerCheckWorker_AssistedFactoryProvider;

    private Provider<AuthApi> provideAuthApiProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<AlignerPlanApi> provideAlignerPlanApiProvider;

    private Provider<PlanRepository> planRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private NonWearTimerDao nonWearTimerDao() {
      return DatabaseModule_ProvideNonWearTimerDaoFactory.provideNonWearTimerDao(provideDatabaseProvider.get());
    }

    private AlignerPlanDao alignerPlanDao() {
      return DatabaseModule_ProvideAlignerPlanDaoFactory.provideAlignerPlanDao(provideDatabaseProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>singletonMap("com.example.bracesaligner.feature.notifications.TimerCheckWorker", ((Provider) timerCheckWorker_AssistedFactoryProvider));
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    private AuthSessionDao authSessionDao() {
      return DatabaseModule_ProvideAuthSessionDaoFactory.provideAuthSessionDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 2));
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 8));
      this.provideSessionStoreProvider = DoubleCheck.provider(new SwitchingProvider<SessionStore>(singletonCImpl, 7));
      this.provideAuthInterceptorProvider = DoubleCheck.provider(new SwitchingProvider<Interceptor>(singletonCImpl, 6));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 5));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 4));
      this.provideTimerApiProvider = DoubleCheck.provider(new SwitchingProvider<TimerApi>(singletonCImpl, 3));
      this.timerRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TimerRepository>(singletonCImpl, 1));
      this.timerCheckWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<TimerCheckWorker_AssistedFactory>(singletonCImpl, 0));
      this.provideAuthApiProvider = DoubleCheck.provider(new SwitchingProvider<AuthApi>(singletonCImpl, 10));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 9));
      this.provideAlignerPlanApiProvider = DoubleCheck.provider(new SwitchingProvider<AlignerPlanApi>(singletonCImpl, 12));
      this.planRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PlanRepository>(singletonCImpl, 11));
    }

    @Override
    public void injectBracesApp(BracesApp bracesApp) {
      injectBracesApp2(bracesApp);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @CanIgnoreReturnValue
    private BracesApp injectBracesApp2(BracesApp instance) {
      BracesApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.bracesaligner.feature.notifications.TimerCheckWorker_AssistedFactory 
          return (T) new TimerCheckWorker_AssistedFactory() {
            @Override
            public TimerCheckWorker create(Context appContext, WorkerParameters workerParams) {
              return new TimerCheckWorker(appContext, workerParams, singletonCImpl.timerRepositoryProvider.get());
            }
          };

          case 1: // com.example.bracesaligner.feature.timer.data.TimerRepository 
          return (T) new TimerRepository(singletonCImpl.nonWearTimerDao(), singletonCImpl.alignerPlanDao(), singletonCImpl.provideTimerApiProvider.get(), singletonCImpl.provideSessionStoreProvider.get());

          case 2: // com.example.bracesaligner.core.database.AppDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.example.bracesaligner.core.network.api.TimerApi 
          return (T) NetworkModule_ProvideTimerApiFactory.provideTimerApi(singletonCImpl.provideRetrofitProvider.get());

          case 4: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 5: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient(singletonCImpl.provideAuthInterceptorProvider.get());

          case 6: // okhttp3.Interceptor 
          return (T) NetworkModule_ProvideAuthInterceptorFactory.provideAuthInterceptor(singletonCImpl.provideSessionStoreProvider.get(), singletonCImpl.provideDatabaseProvider.get());

          case 7: // com.example.bracesaligner.core.preferences.SessionStore 
          return (T) PreferencesModule_ProvideSessionStoreFactory.provideSessionStore(singletonCImpl.provideDataStoreProvider.get());

          case 8: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) PreferencesModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.example.bracesaligner.feature.auth.data.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideAuthApiProvider.get(), singletonCImpl.authSessionDao(), singletonCImpl.provideSessionStoreProvider.get(), singletonCImpl.provideDatabaseProvider.get());

          case 10: // com.example.bracesaligner.core.network.api.AuthApi 
          return (T) NetworkModule_ProvideAuthApiFactory.provideAuthApi(singletonCImpl.provideRetrofitProvider.get());

          case 11: // com.example.bracesaligner.feature.plan.data.PlanRepository 
          return (T) new PlanRepository(singletonCImpl.provideAlignerPlanApiProvider.get(), singletonCImpl.alignerPlanDao(), new ScheduleGenerator());

          case 12: // com.example.bracesaligner.core.network.api.AlignerPlanApi 
          return (T) NetworkModule_ProvideAlignerPlanApiFactory.provideAlignerPlanApi(singletonCImpl.provideRetrofitProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
