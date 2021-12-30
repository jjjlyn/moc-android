package app.moc.android.di

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import app.moc.shared.data.prefs.DataStorePreferencesStorage
import app.moc.shared.data.prefs.PreferencesStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferencesStorageModule {
    val Context.dataStore by preferencesDataStore(
        name = DataStorePreferencesStorage.PREFS_NAME,
        produceMigrations = { context ->
            listOf(
                SharedPreferencesMigration(
                    context,
                    DataStorePreferencesStorage.PREFS_NAME
                )
            )
        }
    )

    @Singleton
    @Provides
    fun providePreferenceStorage(@ApplicationContext context: Context): PreferencesStorage =
        DataStorePreferencesStorage(context.dataStore)
}