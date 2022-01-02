package app.moc.android.di

import app.moc.shared.data.api.AuthService
import app.moc.shared.data.api.BusinessService
import app.moc.shared.data.auth.AuthRepository
import app.moc.shared.data.auth.DefaultAuthRepository
import app.moc.shared.data.business.BusinessRepository
import app.moc.shared.data.business.DefaultBusinessRepository
import app.moc.shared.di.ApplicationScope
import app.moc.shared.di.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

}