package app.moc.shared.domain.signup

import app.moc.model.SignUp
import app.moc.model.User
import app.moc.shared.data.auth.AuthRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.domain.UseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
        private val authRepository: AuthRepository,
        @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<SignUp, User>(ioDispatcher) {
    override fun execute(parameters: SignUp): Flow<Result<User>> {
        return flow {
            emit(Result.Loading)
            emit(Result.Success(authRepository.signUp(parameters)))
        }
    }
}