package app.moc.shared.domain.signup

import app.moc.model.Business
import app.moc.shared.data.business.BusinessRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class BusinessUseCase @Inject constructor(
        private val businessRepository: BusinessRepository,
        @IODispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Unit, List<Business>>(ioDispatcher) {
    override suspend fun execute(parameters: Unit): List<Business> {
        return emptyList()
    }
}