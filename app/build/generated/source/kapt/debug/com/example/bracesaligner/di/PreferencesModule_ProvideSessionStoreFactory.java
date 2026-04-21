package com.example.bracesaligner.di;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.example.bracesaligner.core.preferences.SessionStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class PreferencesModule_ProvideSessionStoreFactory implements Factory<SessionStore> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public PreferencesModule_ProvideSessionStoreFactory(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public SessionStore get() {
    return provideSessionStore(dataStoreProvider.get());
  }

  public static PreferencesModule_ProvideSessionStoreFactory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new PreferencesModule_ProvideSessionStoreFactory(dataStoreProvider);
  }

  public static SessionStore provideSessionStore(DataStore<Preferences> dataStore) {
    return Preconditions.checkNotNullFromProvides(PreferencesModule.INSTANCE.provideSessionStore(dataStore));
  }
}
