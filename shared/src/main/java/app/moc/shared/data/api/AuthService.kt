package app.moc.shared.data.api

import app.moc.shared.data.api.request.SignInRequest
import app.moc.shared.data.api.request.SignUpRequest
import app.moc.shared.data.api.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("login")
    suspend fun signIn(@Body signInRequest: SignInRequest): AuthResponse

    @POST("register")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): AuthResponse

    @GET("checkEmail")
    suspend fun checkEmailDuplicate(@Query("checkEmail") email: String): AuthResponse

    @GET("checkNickName")
    suspend fun checkNickNameDuplicate(@Query("checkNickName") nickName: String): AuthResponse
}