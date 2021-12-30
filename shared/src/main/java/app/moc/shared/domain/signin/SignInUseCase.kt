package app.moc.shared.domain.signin

import app.moc.model.SignIn
import app.moc.model.User
import app.moc.shared.data.auth.AuthRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<SignIn, User>(ioDispatcher) {
    override suspend fun execute(parameters: SignIn): User {
        return authRepository.signIn(parameters)
    }
}