package app.moc.shared.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_BOARDING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface PreferencesStorage {
    suspend fun completeOnBoarding(complete: Boolean)
    val onBoardingCompleted: Flow<Boolean>
}

@Singleton
class DataStorePreferencesStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
): PreferencesStorage {
    companion object {
        const val PREFS_NAME = "moc"
    }

    object PreferencesKey {
        val PREFS_ON_BOARDING = booleanPreferencesKey("pref_on_boarding")
    }

    override suspend fun completeOnBoarding(complete: Boolean) {
        dataStore.edit {
            it[PREFS_ON_BOARDING] = complete
        }
    }

    override val onBoardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[PREFS_ON_BOARDING] ?: false }
}