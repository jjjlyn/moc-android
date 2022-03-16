package app.moc.shared.data.api

import app.moc.shared.data.prefs.PreferencesStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val preferencesStorage: PreferencesStorage
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val userToken = runBlocking {
            preferencesStorage.userToken.first()
        }
        return response.request.newBuilder().removeHeader("Authorization")
            .addHeader("Authorization", userToken).build()
    }
}