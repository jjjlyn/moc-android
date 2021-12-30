package app.moc.shared.data.auth

import app.moc.model.SignIn
import app.moc.model.SignUp
import app.moc.model.User
import app.moc.shared.data.api.AuthService
import app.moc.shared.data.api.request.toRequest
import app.moc.shared.data.api.response.toModel

interface AuthRepository {
    suspend fun signIn(signIn: SignIn): User
    suspend fun signUp(signUp: SignUp): User
    suspend fun checkEmailDuplicate(email: String)
    suspend fun checkNickNameDuplicate(nickName: String)
}

class DefaultAuthRepository(private val service: AuthService) : AuthRepository {
    override suspend fun signIn(signIn: SignIn): User {
        return runCatching {
            service.signIn(signIn.toRequest())
        }.getOrThrow().toModel()
    }

    override suspend fun signUp(signUp: SignUp): User {
        return runCatching {
            service.signUp(signUp.toRequest())
        }.getOrThrow().toModel()
    }

    override suspend fun checkEmailDuplicate(email: String) {
        return kotlin.runCatching {
            service.checkEmailDuplicate(email)
        }.getOrThrow()
    }

    override suspend fun checkNickNameDuplicate(nickName: String) {
        return kotlin.runCatching {
            service.checkNickNameDuplicate(nickName)
        }.getOrThrow()
    }
}