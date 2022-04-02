package app.moc.shared.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import app.moc.model.DateTime
import app.moc.model.User
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_LEAVE_DATE
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_DEVICE_TOKEN
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_EMAIL
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_KEY_WORDS
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_NICK_NAME
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_USER_ID
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_USER_TOKEN
import app.moc.shared.data.prefs.DataStorePreferencesStorage.PreferencesKey.PREFS_ON_USER_TOKEN_EXPIRED_DATE
import app.moc.shared.util.millisDiffToDays
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface PreferencesStorage {
    suspend fun completeOnBoarding(user: User)
    val onBoardingCompleted: Flow<Boolean>
    val userID: Flow<String>
    val userToken: Flow<String>
    val nickName: Flow<String>
    val keyWords: Flow<String>
    val leftDays: Flow<Long>
    val email: Flow<String>
}

@Singleton
class DataStorePreferencesStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
): PreferencesStorage {
    companion object {
        const val PREFS_NAME = "moc"
    }

    object PreferencesKey {
        val PREFS_ON_BOARDING = booleanPreferencesKey("prefs_on_boarding")
        val PREFS_ON_EMAIL = stringPreferencesKey("prefs_on_email")
        val PREFS_ON_NICK_NAME = stringPreferencesKey("prefs_on_nick_name")
        val PREFS_ON_KEY_WORDS = stringPreferencesKey("prefs_on_key_words")
        val PREFS_LEAVE_DATE = longPreferencesKey("prefs_on_leave_date")
        val PREFS_ON_USER_ID = stringPreferencesKey("prefs_on_user_id")
        val PREFS_ON_USER_TOKEN_EXPIRED_DATE = longPreferencesKey("prefs_on_user_token_expired_date")
        val PREFS_ON_USER_TOKEN = stringPreferencesKey("prefs_on_user_token")
        val PREFS_ON_DEVICE_TOKEN = stringPreferencesKey("prefs_on_device_token")
    }

    override suspend fun completeOnBoarding(user: User) {
        dataStore.edit {
            it[PREFS_ON_EMAIL] = user.email ?: ""
            it[PREFS_ON_NICK_NAME] = user.nickName ?: ""
            it[PREFS_ON_KEY_WORDS] = user.keyWords ?: ""
            it[PREFS_LEAVE_DATE] = user.leaveDate * 1000
            it[PREFS_ON_USER_TOKEN_EXPIRED_DATE] = user.userTokenExpiredDate * 1000
            it[PREFS_ON_USER_ID] = user.userId ?: ""
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
    override val userID: Flow<String> =
        dataStore.data.map {
            it[PREFS_ON_USER_ID] ?: return@map ""
        }

    override val userToken: Flow<String> =
        dataStore.data.map {
            it[PREFS_ON_USER_TOKEN] ?: return@map ""
        }

    override val nickName: Flow<String> =
        dataStore.data.map {
            it[PREFS_ON_NICK_NAME] ?: return@map ""
        }

    override val keyWords: Flow<String> =
        dataStore.data.map {
            it[PREFS_ON_KEY_WORDS] ?: return@map ""
        }

    override val leftDays: Flow<Long> =
        dataStore.data.map {
            val leaveDate = it[PREFS_LEAVE_DATE] ?: return@map 0
            millisDiffToDays(leaveDate, DateTime().time)
        }

    override val email: Flow<String> =
        dataStore.data.map {
            it[PREFS_ON_EMAIL] ?: return@map ""
        }
}