package app.moc.android.di

import app.moc.shared.data.api.AuthService
import app.moc.shared.data.api.BusinessService
import app.moc.shared.data.auth.AuthRepository
import app.moc.shared.data.auth.DefaultAuthRepository
import app.moc.shared.data.business.BusinessRepository
import app.moc.shared.data.business.DefaultBusinessRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun authRepository(
        service: AuthService
    ): AuthRepository {
        return DefaultAuthRepository(
            service
        )
    }

    @Provides
    @Singleton
    fun businessRepository(
        service: BusinessService
    ): BusinessRepository {
        return DefaultBusinessRepository(
            service
        )
    }
}