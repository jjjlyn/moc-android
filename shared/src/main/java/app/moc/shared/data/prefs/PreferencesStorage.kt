package app.moc.shared.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.moc.model.User
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_BOARDING
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_DEVICE_TOKEN
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_EMAIL
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_KEY_WORDS
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_NICK_NAME
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_USER_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface PreferencesStorage {
    suspend fun completeOnBoarding(user: User)
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
        val PREFS_ON_EMAIL = stringPreferencesKey("pref_on_email")
        val PREFS_ON_NICK_NAME = stringPreferencesKey("pref_on_nick_name")
        val PREFS_ON_KEY_WORDS = stringPreferencesKey("pref_on_key_words")
        val PREFS_ON_USER_TOKEN = stringPreferencesKey("pref_on_user_token")
        val PREFS_ON_DEVICE_TOKEN = stringPreferencesKey("pref_on_device_token")
    }

    override suspend fun completeOnBoarding(user: User) {
        dataStore.edit {
            it[PREFS_ON_EMAIL] = user.email ?: ""
            it[PREFS_ON_NICK_NAME] = user.nickName ?: ""
            it[PREFS_ON_KEY_WORDS] = user.keyWords ?: ""
            it[PREFS_ON_USER_TOKEN] = user.userToken ?: ""
            it[PREFS_ON_DEVICE_TOKEN] = user.deviceToken ?: ""
        }
    }

    override val onBoardingCompleted: Flow<Boolean> =
        dataStore.data.map {
            val userToken = it[PREFS_ON_USER_TOKEN] ?: return@map false
            val userTokenExpiredDate = it[PREFS_ON_USER_TOKEN_EXPIRED_DATE] ?: return@map false
            userTokenExpiredDate > System.currentTimeMillis() && userToken.isNotEmpty()
        }

    override val userToken: Flow<String> =
        dataStore.data.map {
            it[PREFS_ON_USER_TOKEN] ?: return@map ""
        }
}