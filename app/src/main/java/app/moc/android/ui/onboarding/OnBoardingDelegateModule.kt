package app.moc.android.ui.onboarding

import app.moc.shared.di.ApplicationScope
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.prefs.OnBoardingCompleteActionUseCase
import app.moc.shared.domain.prefs.OnBoardingCompletedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

@InstallIn(ViewModelComponent::class)
@Module
object OnBoardingDelegateModule {

    @Provides
    fun provideOnBoardingDelegate(
        onBoardingCompletedUseCase: OnBoardingCompletedUseCase,
        onBoardingCompleteActionUseCase: OnBoardingCompleteActionUseCase,
        @ApplicationScope applicationScope: CoroutineScope,
        @IODispatcher ioDispatcher: CoroutineDispatcher
    ): OnBoardingDelegate {
        return DefaultOnBoardingDelegate(
            onBoardingCompletedUseCase,
            onBoardingCompleteActionUseCase,
            applicationScope,
            ioDispatcher
        )
    }
}