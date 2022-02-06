package app.moc.shared.di

import app.moc.shared.data.api.AuthService
import app.moc.shared.data.api.BusinessService
import app.moc.shared.data.api.CommunityService
import app.moc.shared.data.api.PlanService
import app.moc.shared.data.auth.AuthRepository
import app.moc.shared.data.auth.DefaultAuthRepository
import app.moc.shared.data.business.BusinessRepository
import app.moc.shared.data.business.DefaultBusinessRepository
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.data.community.DefaultCommunityRepository
import app.moc.shared.data.plan.DefaultPlanRepository
import app.moc.shared.data.plan.PlanRepository
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

    @Provides
    @Singleton
    fun planRepository(
        service: PlanService
    ): PlanRepository {
        return DefaultPlanRepository(
            service
        )
    }

    @Provides
    @Singleton
    fun communityRepository(
        service: CommunityService
    ): CommunityRepository {
        return DefaultCommunityRepository(service)
    }
}