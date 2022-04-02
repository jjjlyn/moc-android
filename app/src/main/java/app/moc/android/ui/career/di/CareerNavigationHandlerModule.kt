package app.moc.android.ui.career.di

import app.moc.android.ui.career.CareerNavigationHandler
import app.moc.android.ui.career.DefaultCareerNavigationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@InstallIn(FragmentComponent::class)
@Module
abstract class CareerNavigationHandlerModule {

    @FragmentScoped
    @Binds
    abstract fun providesCareerNavigationHandlerModule(
        defaultCareerNavigationHandler: DefaultCareerNavigationHandler
    ): CareerNavigationHandler
}